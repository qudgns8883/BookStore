package Spring.Book.domain.admin.product.entity;


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
@Table(name = "product")
public class ProductEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;
    private String author;
    @Column(length = 4000)
    private String description;
    private String category;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int stock;
    private String productImage;
    @Column(length = 2000)
    private String productDetails;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public ProductEntity(String productName, String author, String description, String category, int price, int stock,
                         String productImage, String productDetails,ProductStatus status, UserEntity user) {
        this.productName = productName;
        this.author = author;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.productImage = productImage;
        this.productDetails = productDetails;
        this.status = status;
        this.user = user;
    }
}
