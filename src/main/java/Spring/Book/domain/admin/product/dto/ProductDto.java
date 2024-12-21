package Spring.Book.domain.admin.product.dto;


import Spring.Book.domain.admin.product.entity.ProductEntity;
import Spring.Book.domain.admin.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String productName;
    private String author;
    private String description;
    private String category;
    private int price;
    private int stock;
    private String productImage;
    private String productDetails;
    private ProductStatus productStatus;
    private String createDate;

    @Builder
    public ProductDto(Long id, String productName, String author, String description, String category, int price, int stock, String productImage, String productDetails,ProductStatus productStatus, String createDate) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.productImage = productImage;
        this.productDetails = productDetails;
        this.productStatus = productStatus;
        this.createDate = createDate;
        this.author = author;
    }

    public static ProductDto fromProductEntity(ProductEntity productEntity) {
        return ProductDto.builder()
                .id(productEntity.getId())
                .productName(productEntity.getProductName())
                .author(productEntity.getAuthor())
                .description(productEntity.getDescription())
                .category(productEntity.getCategory())
                .price(productEntity.getPrice())
                .stock(productEntity.getStock())
                .productImage(productEntity.getProductImage())
                .productDetails(productEntity.getProductDetails())
                .productStatus(productEntity.getStatus())
                .createDate(productEntity.getCreateDate().toString())
                .build();
    }
}
