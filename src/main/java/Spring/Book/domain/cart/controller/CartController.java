package Spring.Book.domain.cart.controller;

import Spring.Book.domain.cart.dto.CartDto;
import Spring.Book.domain.cart.service.CartService;
import Spring.Book.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody CartDto cartDTO) {

        cartService.addToCart(cartDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page")
    public String getCartPage(Model model) {
        List<CartDto> cartItems = cartService.getCartItems();

        int totalQuantity = cartItems.stream()
                .mapToInt(CartDto::getQuantity)
                .sum();

        int userMileage = userService.getCurrentUser().getMileage();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartItemCount", cartItems.size());
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("userMileage", userMileage);
        return "payment/cart";
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount() {
        int count = cartService.getCartItemCount();
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId) {
        boolean isDeleted = cartService.deleteCartItem(productId);

        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
