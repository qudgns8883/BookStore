package Spring.Book.domain.order.dto;

import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.order.entity.OrderEntity;
import Spring.Book.domain.payment.entity.PaymentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static Spring.Book.domain.admin.product.dto.ProductDto.fromProductEntity;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private int quantity;
    private int totalAmount;
    private int mileageUsed;
    private ProductDto product;
    private String address;
    private String deliveryInstructions;
    private String paymentId;
    private String createDate;

    @Builder
    public OrderDto(Long id, int quantity, ProductDto product, String paymentId, int totalAmount, int mileageUsed, String address, String deliveryInstructions,  String createDate){
        this.id = id;
        this.quantity = quantity;
        this.paymentId = paymentId;
        this.totalAmount = totalAmount;
        this.mileageUsed = mileageUsed;
        this.address = address;
        this.product = product;
        this.deliveryInstructions = deliveryInstructions;
        this.createDate = createDate;
    }

    // OrderDto 변환 메서드
    public static OrderDto from(OrderEntity orderItem, PaymentEntity payment) {
        ProductDto productDto = fromProductEntity(orderItem.getProduct());

        return OrderDto.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .paymentId(payment.getOrderId())
                .totalAmount(payment.getAmount())
                .mileageUsed(payment.getMileageUsed())
                .address(payment.getShippingAddress())
                .deliveryInstructions(payment.getDeliveryInstructions())
                .product(productDto)
                .createDate(orderItem.getCreateDateAsString())
                .build();
    }
}
