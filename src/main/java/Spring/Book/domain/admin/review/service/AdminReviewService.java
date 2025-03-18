package Spring.Book.domain.admin.review.service;

import Spring.Book.domain.admin.product.entity.QProductEntity;
import Spring.Book.domain.admin.review.dto.ReviewStatusCount;
import Spring.Book.domain.admin.review.repository.AdminReviewRepository;
import Spring.Book.domain.review.dto.ReviewDto;
import Spring.Book.domain.review.entity.QReviewEntity;
import Spring.Book.domain.review.entity.ReviewEntity;
import Spring.Book.domain.user.dto.UserStatusCount;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final AdminReviewRepository adminReviewRepository;
    private final JPAQueryFactory queryFactory;
    private final QReviewEntity review = QReviewEntity.reviewEntity;
    private final QProductEntity product = QProductEntity.productEntity;

    public List<ReviewDto> findall(){
        List<ReviewEntity> reviews = adminReviewRepository.findAllWithProduct();

        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> findReviewsByFilter(String status, String date, String query) {
        // 각 조건을 BooleanExpression으로 처리
        BooleanExpression predicate = Expressions.asBoolean(true).isTrue() // 기본적으로 true로 시작
                .and(eqStatus(status))
                .and(eqDate(date))
                .and(containsQuery(query));

        // 쿼리 실행
        return queryFactory
                .select(Projections.constructor(ReviewDto.class,
                        review.id,
                        review.product.id,
                        review.product.productName,
                        review.review,
                        review.rating,
                        review.author,
                        review.status,
                        review.answer,
                        review.createDate
                ))
                .from(review)
                .join(review.product, product)
                .where(predicate)
                .fetch();
    }

    private BooleanExpression eqStatus(String status) {
        return (StringUtils.hasText(status) && !"전체리뷰".equals(status))
                ? review.status.eq(status)
                : null;
    }

    private BooleanExpression eqDate(String date) {
        if (StringUtils.hasText(date) && !"전체리뷰".equals(date)) {
            LocalDate filterDate = LocalDate.parse(date);
            LocalDateTime startDate = filterDate.atStartOfDay(); // 날짜의 시작 시간
            LocalDateTime endDate = LocalDateTime.now(); // 현재 시점
            return review.createDate.between(startDate, endDate);
        }
        return null;
    }

    private BooleanExpression containsQuery(String query) {
        return StringUtils.hasText(query)
                ? review.product.productName.containsIgnoreCase(query)
                : null;
    }

//    public List<ReviewDto> findReviewsByFilter(String status, String date, String query) {
//
//        List<ReviewEntity> reviews = adminReviewRepository.findAllWithProduct();
//
//        if (!"전체리뷰".equals(status)) {
//            reviews = reviews.stream()
//                    .filter(review -> review.getStatus().equals(status))
//                    .collect(Collectors.toList());
//        }
//
//        if (!"전체리뷰".equals(date)) {
//            LocalDate filterDate = LocalDate.parse(date);
//            LocalDateTime now = LocalDateTime.now();
//
//            reviews = reviews.stream()
//                    .filter(review -> {
//                        LocalDateTime reviewDate = review.getCreateDate();
//                        if (filterDate.equals(LocalDate.now())) {
//                            return reviewDate.toLocalDate().equals(LocalDate.now());
//                        } else {
//                            return !reviewDate.toLocalDate().isBefore(filterDate) && !reviewDate.toLocalDate().isAfter(now.toLocalDate());
//                        }
//                    })
//                    .collect(Collectors.toList());
//        }
//
//        if (!query.isEmpty()) {
//            reviews = reviews.stream()
//                    .filter(review -> review.getProduct().getProductName().toLowerCase().contains(query.toLowerCase()))
//                    .collect(Collectors.toList());
//        }
//
//        return reviews.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

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
                .createDate(review.getCreateDate())
                .build();
    }

    public ReviewStatusCount countReviewStatus() {
        return adminReviewRepository.countReviewStatus();
    }
}
