package Spring.Book.domain.review.controller;

import Spring.Book.domain.review.dto.ReviewDto;
import Spring.Book.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/addReview")
    public ResponseEntity<List<ReviewDto>> submitReview(@RequestBody ReviewDto reviewDto) {

        if (!reviewService.hasPurchased(reviewDto.getProductId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (reviewService.hasReviewed(reviewDto.getProductId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        reviewService.submitReview(reviewDto);
        List<ReviewDto> reviews = reviewService.findReviewsByProductId(reviewDto.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(reviews);
    }

    @GetMapping("/getReviews")
    public ResponseEntity<List<ReviewDto>> getReviews() {
        List<ReviewDto> reviews = reviewService.getReviewsByUserId();
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
