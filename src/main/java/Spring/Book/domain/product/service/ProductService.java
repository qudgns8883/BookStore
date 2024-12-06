package Spring.Book.domain.product.service;


import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto findById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

        return ProductDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .author(product.getAuthor())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(product.getStock())
                .productImage(product.getProductImage())
                .productDetails(product.getProductDetails())
                .productStatus(product.getStatus())
                .createDate(product.getCreateDateAsString())
                .build();
    }
}
