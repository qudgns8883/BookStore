package Spring.Book.domain.review.service;

import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.notification.dto.KafkaMessageDto;
import Spring.Book.domain.notification.service.KafkaProducer;
import Spring.global.event.ReviewEvent;
import Spring.Book.domain.order.repository.OrderRepository;
import Spring.Book.domain.product.repository.ProductRepository;
import Spring.Book.domain.review.dto.ReviewDto;
import Spring.Book.domain.review.entity.ReviewEntity;
import Spring.Book.domain.review.repository.ReviewRepository;
import Spring.Book.domain.user.entity.Role;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import Spring.Book.domain.user.service.UserService;
import Spring.global.aspect.Loggable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public void submitReview(ReviewDto reviewDto) {

        UserEntity user = userService.getCurrentUser();

        ProductEntity product = productRepository.findById(reviewDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("제품이 존재하지 않습니다. 제품 ID: " + reviewDto.getProductId()));

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .review(reviewDto.getReview())
                .rating(reviewDto.getRating())
                .status("미답변")
                .author(user.getNickname())
                .answer(reviewDto.getAnswer())
                .product(product)
                .build();

        user.addReview(reviewEntity);
        reviewRepository.save(reviewEntity);

        List<UserEntity> admins = userRepository.findByRole(Role.ADMIN);

        if (admins.isEmpty()) {
            throw new IllegalArgumentException("관리자를 찾을 수 없음");
        }

        List<Long> adminIds = admins.stream().map(UserEntity::getId).toList();

        String notificationMessage = user.getNickname() + "님이 '" + product.getProductName() + "' 제품에 리뷰를 남겼습니다.";

        KafkaMessageDto message = new KafkaMessageDto(notificationMessage, adminIds);
        kafkaProducer.sendNotification(message);

        reviewDto.populateFromReview(reviewEntity, user, product);
    }

    public boolean hasPurchased(Long productId) {
        UserEntity user = userService.getCurrentUser();
        return orderRepository.existsByUserIdAndProductId(user.getId(), productId);
    }

    public boolean hasReviewed(Long productId) {
        UserEntity user = userService.getCurrentUser();
        return reviewRepository.existsByProductIdAndUserId(productId, user.getId());
    }

    public List<ReviewDto> findReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(this::ReviewDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByUserId() {
        UserEntity user = userService.getCurrentUser();

        List<ReviewEntity> reviews = reviewRepository.findByUserId(user.getId());
        return reviews.stream()
                .map(this::ReviewDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. ID: " + reviewId));
        reviewRepository.delete(review);
    }

    private ReviewDto ReviewDto(ReviewEntity review) {
        return ReviewDto.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .productName(review.getProduct().getProductName())
                .review(review.getReview())
                .rating(review.getRating())
                .author(review.getAuthor())
                .status(review.getStatus())
                .answer(review.getAnswer())
                .createDate(review.getCreateDate())
                .build();
    }
}
