package Spring.Book.domain.admin.product.repository;

import Spring.Book.domain.admin.product.dto.ProductStatusCount;
import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.admin.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT new Spring.Book.domain.admin.product.dto.ProductStatusCount(" +
            "COUNT(CASE WHEN p.status = '판매중' THEN 1 END), " +
            "COUNT(CASE WHEN p.status = '판매중지' THEN 1 END), " +
            "COUNT(CASE WHEN p.status = '품절' THEN 1 END)) " +
            "FROM ProductEntity p")
    ProductStatusCount countProductStatus();

    List<ProductEntity> findByStatus(ProductStatus status);
}
