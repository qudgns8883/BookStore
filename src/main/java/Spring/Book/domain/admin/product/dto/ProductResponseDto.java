package Spring.Book.domain.admin.product.dto;

import Spring.Book.domain.admin.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class ProductResponseDto  {
    private Long id;
    private String productName;
    private String category;
    private int price;
    private int stock;
    private ProductStatus productStatus;
    private String createDate;

    @Builder
    public ProductResponseDto(Long id, String productName, String category,
                      int price, int stock, ProductStatus productStatus, LocalDateTime createDate) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.productStatus = productStatus;
        this.createDate = createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
