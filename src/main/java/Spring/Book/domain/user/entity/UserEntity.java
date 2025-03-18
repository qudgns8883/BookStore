package Spring.Book.domain.user.entity;

import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.cart.entity.CartEntity;
import Spring.Book.domain.order.entity.OrderEntity;
import Spring.Book.domain.payment.entity.PaymentEntity;
import Spring.Book.domain.review.entity.ReviewEntity;
import Spring.Book.domain.user.dto.Address;
import Spring.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @NotEmpty
    @Column(unique = true)
    private String nickname;
    @NotEmpty
    private String password;
    @NotEmpty
    private String username;
    @NotEmpty
    private String birthdate;
    @Setter
    private Integer mileage;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<CartEntity> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<PaymentEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<OrderEntity> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ReviewEntity> reviews = new ArrayList<>();

    @Builder(toBuilder = true)
    private UserEntity(Long id, String nickname, String password, String username,
                       String birthdate, Integer mileage, Role role, Address address, Status status,
                       List<ProductEntity> products, List<CartEntity> carts,
                       List<PaymentEntity> payments, List<OrderEntity> orders, List<ReviewEntity> reviews) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.username = username;
        this.birthdate = birthdate;
        this.mileage = mileage;
        this.role = role;
        this.address = address;
        this.status = status;
        this.products =  products ;
        this.carts = carts;
        this.payments = payments;
        this.orders = orders;
        this.reviews = reviews;
    }

    public String getRoleAsString() {
        return role.name();
    }

    public void addProduct(ProductEntity product) {
        product.setUser(this);
        products.add(product);
    }

    public void addCart(CartEntity cart){
        cart.setUser(this);
        carts.add(cart);
    }

    @PrePersist
    public void prePersist() {
        if (mileage == null) {
            mileage = 0;
        }
    }

    public void addPayment(PaymentEntity payment){
        payment.setUser(this);
        payments.add(payment);
    }

    public void addReview(ReviewEntity review){
        review.setUser(this);
        reviews.add(review);
    }

}
