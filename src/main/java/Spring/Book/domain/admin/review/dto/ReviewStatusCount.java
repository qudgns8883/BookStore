package Spring.Book.domain.admin.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewStatusCount {
    private long answeredCount;
    private long unansweredCount;
}
