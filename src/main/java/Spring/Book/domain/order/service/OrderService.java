package Spring.Book.domain.order.service;

import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.cart.dto.CartDto;
import Spring.Book.domain.order.dto.OrderDto;
import Spring.Book.domain.order.entity.OrderEntity;
import Spring.Book.domain.order.repository.OrderRepository;
import Spring.Book.domain.payment.dto.PaymentRequest;
import Spring.Book.domain.payment.entity.PaymentEntity;
import Spring.Book.domain.payment.repository.PaymentRepository;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final UserService userService;

    public List<OrderDto> getOrder(String orderId){

        PaymentEntity payment = paymentRepository.findByOrderId(orderId);

        List<OrderEntity> orderItems = orderRepository.findByPayment(payment);
        return orderItems.stream()
                .map(orderItem -> OrderDto.from(orderItem, payment))
                .collect(Collectors.toList());
    }

    public List<OrderDto> getOrders() {
        UserEntity user = userService.getCurrentUser();
        List<OrderEntity> orderItems = orderRepository.findOrdersByUser(user.getId());

        return orderItems.stream()
                .map(orderItem -> OrderDto.from(orderItem, orderItem.getPayment()))
                .collect(Collectors.toList());
    }
}
