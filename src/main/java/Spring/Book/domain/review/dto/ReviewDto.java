package Spring.Book.domain.review.dto;


import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.review.entity.ReviewEntity;
import Spring.Book.domain.user.entity.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    @NotNull(message = "제품 ID는 필수입니다.")
    private Long productId;
    private String productName;
    @NotEmpty(message = "리뷰 내용은 필수입니다.")
    @Size(max = 100, message = "리뷰 내용은 100자 이하여야 합니다.")
    private String review;
    @NotNull(message = "평점은 필수입니다.")
    private Integer rating;
    private String author;
    private String status;
    private String answer;
    private String createDate;

    @Builder
    public ReviewDto(Long id, Long productId, String productName, String review, int rating,
                     String author, String status, String answer, LocalDateTime createDate){
        this.id = id;
        this.productId = productId;
        this.review = review;
        this.rating = rating;
        this.author = author;
        this.productName = productName;
        this.status = status;
        this.answer = answer;
        this.createDate = createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void populateFromReview(ReviewEntity review, UserEntity user, ProductEntity product) {
        this.author = user.getNickname();
        this.productName = product.getProductName();
        this.id = review.getId();
        this.createDate = review.getCreateDateAsString();
    }
}
