package Spring.Book.domain.payment.service;

import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.admin.product.entity.ProductStatus;
import Spring.Book.domain.cart.repository.CartRepository;
import Spring.Book.domain.notification.dto.KafkaMessageDto;
import Spring.Book.domain.notification.service.KafkaProducer;
import Spring.Book.domain.order.entity.OrderEntity;
import Spring.Book.domain.order.repository.OrderRepository;
import Spring.Book.domain.payment.dto.PaymentRequest;
import Spring.Book.domain.payment.entity.PaymentEntity;
import Spring.Book.domain.payment.repository.PaymentRepository;
import Spring.Book.domain.product.repository.ProductRepository;
import Spring.Book.domain.user.entity.Role;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import Spring.Book.domain.user.service.UserService;
import Spring.global.aspect.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import Spring.Book.domain.cart.dto.CartDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserService userService;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaProducer kafkaProducer;

    @Loggable("ORDER_PAYMENT")
    @Transactional
    public String saveOrder(PaymentRequest paymentRequest) {
        UserEntity user = userService.getCurrentUser();
        validateUserMileage(user, paymentRequest.getMileageUsed());

        PaymentEntity payment = createPayment(paymentRequest, user);

        processOrderAndStock(paymentRequest, user, payment);

        updateUserMileage(user, paymentRequest.getMileageUsed(), paymentRequest.getEarnedMileage());

        return "결제 정보가 저장되었습니다.";
    }

    private void validateUserMileage(UserEntity user, int mileageUsed) {
        if (user.getMileage() < mileageUsed) {
            throw new IllegalArgumentException("사용할 수 있는 마일리지가 부족합니다.");
        }
    }

    private PaymentEntity createPayment(PaymentRequest paymentRequest, UserEntity user) {

        PaymentEntity payment = PaymentEntity.builder()
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .recipientName(paymentRequest.getRecipientName())
                .shippingAddress(paymentRequest.getShippingAddress())
                .deliveryInstructions(paymentRequest.getDeliveryInstructions())
                .mileageUsed(paymentRequest.getMileageUsed())
                .build();

        user.addPayment(payment);
        paymentRepository.save(payment);

        return payment;
    }

    private void processOrderAndStock(PaymentRequest paymentRequest, UserEntity user, PaymentEntity payment) {
        List<String> productSummaries = new ArrayList<>();
        int totalQuantity = 0;

        for (CartDto item : paymentRequest.getCartDto()) {
            ProductEntity product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("재고가 부족합니다.");
            }

            OrderEntity order = OrderEntity.builder()
                    .payment(payment)
                    .quantity(item.getQuantity())
                    .product(product)
                    .user(user)
                    .build();

            orderRepository.save(order);

            product.decreaseStock(item.getQuantity());

            if (product.getStock() == 0) {
                product.setStatus(ProductStatus.품절);
            }

            productRepository.save(product);

            cartRepository.deleteByUserIdAndProductId(user.getId(), item.getProductId());

            productSummaries.add(product.getProductName() + " " + item.getQuantity() + "개");
            totalQuantity += item.getQuantity();
        }

        List<UserEntity> admins = userRepository.findByRole(Role.ADMIN);

        List<Long> adminIds = admins.stream().map(UserEntity::getId).toList();

        if (admins.isEmpty()) {
            throw new IllegalArgumentException("관리자를 찾을 수 없음");
        }

        String productMessage = String.join(", ", productSummaries);

        String notificationMessage = user.getNickname() + "님이 총 " + productSummaries.size() + "개의 상품을 "
                + totalQuantity + "개 주문했습니다. (" + productMessage + ")";

        KafkaMessageDto message = new KafkaMessageDto(notificationMessage, adminIds);
        kafkaProducer.sendNotification(message);
    }

    private void updateUserMileage(UserEntity user, int mileageUsed, int earnedMileage) {

        UserEntity updatedUser = user.toBuilder()
                .mileage(user.getMileage() - mileageUsed + earnedMileage)
                .products(user.getProducts())
                .payments(user.getPayments())
                .orders(user.getOrders())
                .build();

        userRepository.save(updatedUser);
    }
}