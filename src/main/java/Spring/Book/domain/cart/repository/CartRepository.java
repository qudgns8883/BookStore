package Spring.Book.domain.cart.repository;

import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.cart.entity.CartEntity;
import Spring.Book.domain.user.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    CartEntity findByUserAndProduct(UserEntity user, ProductEntity product);
    List<CartEntity> findByUserId(Long userId);
    Optional<CartEntity> findByUserIdAndProductId(Long userId, Long productId);
    @Query("SELECT c FROM CartEntity c JOIN FETCH c.product WHERE c.user.id = :userId")
    List<CartEntity> findByUserIdWithProducts(@Param("userId") Long userId);
    void deleteByUserIdAndProductId(Long userId, Long productId);
}
