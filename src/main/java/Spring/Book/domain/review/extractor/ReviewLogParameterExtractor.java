package Spring.Book.domain.review.extractor;

import Spring.Book.domain.review.dto.ReviewDto;
import Spring.global.aspect.LogParameterExtractor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("REVIEW_CREATED")
public class ReviewLogParameterExtractor implements LogParameterExtractor {

    @Override
    public Map<String, Object> extractParameters(Object[] args) {
        Map<String, Object> paramData = new HashMap<>();

        if (args.length == 1 && args[0] instanceof ReviewDto reviewDto) {
            paramData.put("reviewId", reviewDto.getId());
            paramData.put("productId", reviewDto.getProductId());
            paramData.put("productName", reviewDto.getProductName());
            paramData.put("reviewText", reviewDto.getReview());
            paramData.put("rating", reviewDto.getRating());
            paramData.put("author", reviewDto.getAuthor());
            paramData.put("createDate", reviewDto.getCreateDate());
        }

        return paramData;
    }
}