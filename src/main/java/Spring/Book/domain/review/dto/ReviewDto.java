package Spring.Book.domain.review.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
    public ReviewDto(Long id, Long productId, String review, int rating, String status,String author, String productName, String answer ,String createDate){
        this.id = id;
        this.productId = productId;
        this.review = review;
        this.rating = rating;
        this.author = author;
        this.productName = productName;
        this.status = status;
        this.answer = answer;
        this.createDate = createDate;
    }
}
