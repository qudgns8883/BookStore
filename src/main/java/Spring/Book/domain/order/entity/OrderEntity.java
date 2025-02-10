package Spring.Book.domain.order.entity;

import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.payment.entity.PaymentEntity;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder(toBuilder = true)
    public OrderEntity(PaymentEntity payment, int quantity, ProductEntity product, UserEntity user) {
        this.payment = payment;
        this.quantity = quantity;
        this.product = product;
        this.user = user;
    }
}
