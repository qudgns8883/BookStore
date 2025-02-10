package Spring.Book.domain.review.repository;

import Spring.Book.domain.review.entity.ReviewEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByProductId(Long productId);
    boolean existsByProductIdAndUserId(Long productId, Long userId);
    @Query("SELECT r FROM ReviewEntity r JOIN FETCH r.product WHERE r.user.id = :userId")
    List<ReviewEntity> findByUserId(@Param("userId") Long userId);
}
