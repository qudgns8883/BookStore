package Spring.Book.domain.cart.dto;

import Spring.Book.domain.admin.product.dto.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartDto {

    private Long productId;
    private int quantity;
    private ProductDto product;

    @Builder
    public CartDto(Long productId, int quantity, ProductDto product){
        this.productId = productId;
        this.quantity = quantity;
        this.product =product;
    }
}
