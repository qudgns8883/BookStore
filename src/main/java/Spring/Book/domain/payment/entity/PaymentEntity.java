package Spring.Book.domain.payment.entity;

import Spring.Book.domain.cart.dto.CartDto;
import Spring.Book.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private int amount;
    private String recipientName;
    private String shippingAddress;
    private String deliveryInstructions;
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

    public void setPaymentUser(UserEntity user) {
        this.user = user;
    }
}
