package Spring.Book.domain.cart.entity;


import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cart")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private int quantity;

    @Builder
    public CartEntity(UserEntity user, ProductEntity product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }
}
