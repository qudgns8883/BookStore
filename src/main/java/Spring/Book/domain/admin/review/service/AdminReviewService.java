package Spring.Book.domain.admin.review.service;

import Spring.Book.domain.admin.review.dto.ReviewStatusCount;
import Spring.Book.domain.admin.review.repository.AdminReviewRepository;
import Spring.Book.domain.review.dto.ReviewDto;
import Spring.Book.domain.review.entity.ReviewEntity;
import Spring.Book.domain.user.dto.UserStatusCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final AdminReviewRepository adminReviewRepository;

    public List<ReviewDto> findall(){
        List<ReviewEntity> reviews = adminReviewRepository.findAllWithProduct();

        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> findReviewsByFilter(String status, String date, String query) {

        List<ReviewEntity> reviews = adminReviewRepository.findAllWithProduct();

        if (!"전체리뷰".equals(status)) {
            reviews = reviews.stream()
                    .filter(review -> review.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        if (!"전체리뷰".equals(date)) {
            LocalDate filterDate = LocalDate.parse(date);
            LocalDateTime now = LocalDateTime.now();

            reviews = reviews.stream()
                    .filter(review -> {
                        LocalDateTime reviewDate = review.getCreateDate();
                        if (filterDate.equals(LocalDate.now())) {
                            return reviewDate.toLocalDate().equals(LocalDate.now());
                        } else {
                            return !reviewDate.toLocalDate().isBefore(filterDate) && !reviewDate.toLocalDate().isAfter(now.toLocalDate());
                        }
                    })
                    .collect(Collectors.toList());
        }

        if (!query.isEmpty()) {
            reviews = reviews.stream()
                    .filter(review -> review.getProduct().getProductName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void addAnswer(Long reviewId, String answer) {

        ReviewEntity review = adminReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        ReviewEntity updatedReview = ReviewEntity.builder()
                .id(review.getId())
                .author(review.getAuthor())
                .review(review.getReview())
                .rating(review.getRating())
                .status("답변")
                .answer(answer)
                .product(review.getProduct())
                .user(review.getUser())
                .build();

        adminReviewRepository.save(updatedReview);

    }

    private ReviewDto convertToDto(ReviewEntity review) {
        return ReviewDto.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .review(review.getReview())
                .rating(review.getRating())
                .author(review.getAuthor())
                .productName(review.getProduct() != null ? review.getProduct().getProductName() : "")
                .status(review.getStatus())
                .answer(review.getAnswer())
                .createDate(review.getCreateDateAsString())
                .build();
    }

    public ReviewStatusCount countReviewStatus() {
        return adminReviewRepository.countReviewStatus();
    }
}
