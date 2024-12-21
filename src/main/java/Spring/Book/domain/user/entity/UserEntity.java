package Spring.Book.domain.user.entity;

import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.cart.entity.CartEntity;
import Spring.Book.domain.order.entity.OrderEntity;
import Spring.Book.domain.payment.entity.PaymentEntity;
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
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<CartEntity> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<PaymentEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<OrderEntity> orderEntities = new ArrayList<>();

    @Builder(toBuilder = true)
    private UserEntity(Long id, String nickname, String password, String username,
                       String birthdate, Integer mileage, Role role, Address address,
                       List<ProductEntity> products, List<CartEntity> carts,
                       List<PaymentEntity> payments, List<OrderEntity> orderEntities) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.username = username;
        this.birthdate = birthdate;
        this.mileage = mileage;
        this.role = role;
        this.address = address;
        this.products =  products ;
        this.carts = carts;
        this.payments = payments; // null 체크
        this.orderEntities = orderEntities; // null 체크
    }

    public String getRoleAsString() {
        return role.name();
    }

    // 연관관계 편의 메서드
    public void addProduct(ProductEntity product) {
        product.setUser(this);
        products.add(product);
    }

    public void addCart(CartEntity cart){
        cart.setUser(this);
        carts.add(cart);
    }

    public void addPayment(PaymentEntity payment){
        payment.setUser(this);
        payments.add(payment);
    }

}
