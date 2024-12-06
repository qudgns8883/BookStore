package Spring.Book.domain.cart.dto;

import Spring.Book.domain.admin.product.dto.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartDto {

    private Long productId;
    private int quantity;
    private ProductDto product;
}
