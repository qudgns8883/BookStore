package Spring.Book.domain.order.controller;

import Spring.Book.domain.order.dto.OrderDto;
import Spring.Book.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/success")
    public String paymentSuccessPage(@RequestParam("orderId") String orderId, Model model) {
        List<OrderDto> order = orderService.getOrder(orderId);

        model.addAttribute("order", order);
        return "payment/paymentSuccess";
    }
}
