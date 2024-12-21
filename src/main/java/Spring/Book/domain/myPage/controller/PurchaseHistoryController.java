package Spring.Book.domain.myPage.controller;

import Spring.Book.domain.order.dto.OrderDto;
import Spring.Book.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class PurchaseHistoryController  {

    private final OrderService orderService;

    @GetMapping("/getOrder")
    public ResponseEntity<List<OrderDto>> getOrder(Model model) {

        List<OrderDto> orders = orderService.getOrders();

        return ResponseEntity.ok(orders);
    }
}
