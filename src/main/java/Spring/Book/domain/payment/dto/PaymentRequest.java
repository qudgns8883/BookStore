package Spring.Book.domain.payment.dto;

import Spring.Book.domain.cart.dto.CartDto;
import Spring.Book.domain.payment.entity.PaymentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class PaymentRequest {

    private String orderId;
    private int amount;
    private String recipientName;
    private String shippingAddress;
    private String deliveryInstructions;
    private List<CartDto> cartDto = new ArrayList<>();
    private int mileageUsed;
    private int earnedMileage;

    public PaymentRequest(String orderId, int amount, String recipientName, String shippingAddress, String deliveryInstructions, List<CartDto> cartDto, int mileageUsed, int earnedMileage) {
        this.orderId = orderId;
        this.amount = amount;
        this.recipientName = recipientName;
        this.shippingAddress = shippingAddress;
        this.deliveryInstructions = deliveryInstructions;
        this.cartDto = cartDto;
        this.mileageUsed = mileageUsed;
        this.earnedMileage = earnedMileage;
    }

    public PaymentEntity toPaymentEntity() {
        return PaymentEntity.builder()
                .orderId(this.orderId)
                .amount(this.amount)
                .recipientName(this.recipientName)
                .shippingAddress(this.shippingAddress)
                .deliveryInstructions(this.deliveryInstructions)
                .mileageUsed(this.mileageUsed)
                .build();
    }
}
