package Spring.Book.domain.order.repository;

import Spring.Book.domain.order.entity.OrderEntity;
import Spring.Book.domain.payment.entity.PaymentEntity;
import Spring.Book.domain.user.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByPayment(PaymentEntity payment);
    @Query("SELECT o FROM OrderEntity o JOIN FETCH o.payment JOIN FETCH o.product WHERE o.user.id = :userId")
    List<OrderEntity> findOrdersByUser(@Param("userId") Long userId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
