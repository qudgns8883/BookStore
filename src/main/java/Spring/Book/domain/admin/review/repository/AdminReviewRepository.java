package Spring.Book.domain.admin.review.repository;

import Spring.Book.domain.admin.review.dto.ReviewStatusCount;
import Spring.Book.domain.review.entity.ReviewEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdminReviewRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("SELECT r FROM ReviewEntity r " +
            "JOIN FETCH r.product p ")
    List<ReviewEntity> findAllWithProduct();

    @Query("SELECT new Spring.Book.domain.admin.review.dto.ReviewStatusCount(" +
            "COUNT(CASE WHEN r.status = '답변' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = '미답변' THEN 1 END)) " +
            "FROM ReviewEntity r")
    ReviewStatusCount countReviewStatus();

}
