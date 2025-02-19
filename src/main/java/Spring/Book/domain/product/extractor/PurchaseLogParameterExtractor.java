package Spring.Book.domain.product.extractor;


import Spring.Book.domain.admin.product.dto.ProductDto;
import Spring.Book.domain.cart.dto.CartDto;
import Spring.Book.domain.payment.dto.PaymentRequest;
import Spring.global.aspect.LogParameterExtractor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("ORDER_PAYMENT")
public class PurchaseLogParameterExtractor implements LogParameterExtractor {

    @Override
    public Map<String, Object> extractParameters(Object[] args) {

        Map<String, Object> paramData = new HashMap<>();

        if (args.length == 1 && args[0] instanceof PaymentRequest request) {
            paramData.put("orderId", request.getOrderId());
            paramData.put("amount", request.getAmount());
            paramData.put("mileageUsed", request.getMileageUsed());
            paramData.put("earnedMileage", request.getEarnedMileage());
            paramData.put("cartItems", extractCartItems(request.getCartDto()));
        }
        return paramData;
    }


    private List<Map<String, Object>> extractCartItems(List<CartDto> cartDtoList) {

        List<Map<String, Object>> cartItems = new ArrayList<>();

        for (CartDto item : cartDtoList) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("productId", item.getProduct().getId());
            itemData.put("productName", item.getProduct().getProductName());
            itemData.put("category", item.getProduct().getCategory());
            itemData.put("price", item.getProduct().getPrice());
            itemData.put("quantity", item.getQuantity());
            cartItems.add(itemData);
        }
        return cartItems;
    }
}