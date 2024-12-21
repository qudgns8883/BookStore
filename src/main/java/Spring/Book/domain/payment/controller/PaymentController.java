package Spring.Book.domain.payment.controller;

import Spring.Book.domain.cart.dto.CartDto;
import Spring.Book.domain.cart.service.CartService;
import Spring.Book.domain.payment.dto.PaymentRequest;
import Spring.Book.domain.payment.service.PaymentService;
import Spring.Book.domain.user.dto.UserDto;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final CartService cartService;
    private final UserService userService;
    private final PaymentService paymentService;

    @GetMapping
    public String paymentPage(@RequestParam String totalQuantity,
                              @RequestParam int totalPrice,
                              @RequestParam(defaultValue = "0") int mileageUsed,
                              @RequestParam int totalAmount,
                              Model model) {

        UserDto user = userService.getCurrentUserinfo();

        List<CartDto> cartItems = cartService.getCartItems();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartItemCount", cartItems.size());
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("mileageUsed", mileageUsed);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("user", user);

        return "payment/paymentPage";
    }
        @PostMapping("/saveOrder")
        public ResponseEntity<?> savePayment(@RequestBody PaymentRequest paymentRequest) {

            paymentService.saveOrder(paymentRequest);

            return ResponseEntity.ok("결제 정보가 저장되었습니다.");
        }

}


