package Spring.Book.domain.review.entity;

import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Review")
public class ReviewEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 500)
    private String review;
    @Column(nullable = false)
    private int rating;
    @Column(nullable = false)
    private String author;
    private String status;
    private String answer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "user_id")
    private UserEntity user;

    @Builder
    public ReviewEntity(Long id, String review, int rating, String author, String status, ProductEntity product, String answer, UserEntity user) {
        this.id = id;
        this.review = review;
        this.rating = rating;
        this.author = author;
        this.status = status;
        this.product = product;
        this.answer = answer;
        this.user = user;

    }

    public void setReviewUser(UserEntity user) {
        this.user = user;
    }
}
