package Spring.Book.domain.admin.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ProductStatusCount {
    private Long onSaleCount;
    private Long stopSellingCount;
    private Long outOfStockCount;

}
