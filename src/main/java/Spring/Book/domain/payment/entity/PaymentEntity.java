package Spring.Book.domain.payment.entity;

import Spring.Book.domain.cart.dto.CartDto;
import Spring.Book.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키
    private String orderId; // 주문 고유 ID
    private int amount; // 결제 금액
    private String recipientName; // 받는 사람 이름
    private String shippingAddress; // 배송 주소
    private String deliveryInstructions; // 배송 요청 사항
    private int mileageUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public PaymentEntity(String orderId, int amount, String recipientName, String shippingAddress, String deliveryInstructions, UserEntity user, int mileageUsed) {
        this.orderId = orderId;
        this.amount = amount;
        this.recipientName = recipientName;
        this.shippingAddress = shippingAddress;
        this.deliveryInstructions = deliveryInstructions;
        this.user = user;
        this.mileageUsed = mileageUsed;
    }
}
