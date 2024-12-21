package Spring.Book.domain.payment.dto;

import Spring.Book.domain.cart.dto.CartDto;
import Spring.Book.domain.payment.entity.PaymentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class PaymentRequest {

    private String orderId; // 주문 고유 ID
    private int amount; // 결제 금액
    private String recipientName; // 받는 사람 이름
    private String shippingAddress; // 배송 주소
    private String deliveryInstructions; // 배송 요청 사항
    private List<CartDto> cartDto = new ArrayList<>(); // 상품 ID와 수량을 포함하는 리스트
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

    // PaymentEntity 변환 메서드
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
