package Spring.Book.domain.myPage.controller;

import Spring.Book.domain.myPage.service.MyPageService;
import Spring.Book.domain.order.dto.OrderDto;
import Spring.Book.domain.order.service.OrderService;
import Spring.Book.domain.user.dto.UserDto;
import Spring.Book.domain.user.service.SecurityService;
import Spring.Book.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController {

    private final OrderService orderService;
    private final UserService userService;
    private final MyPageService myPageService;
    private final SecurityService securityService;

    @GetMapping
    public String myPage(){
        return "myPage/myPage";
    }

    @GetMapping("/getOrder")
    public ResponseEntity<List<OrderDto>> getOrder(Model model) {

        List<OrderDto> orders = orderService.getOrders();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/getUserInfo")
    public ResponseEntity<UserDto> getUserInfo() {
        UserDto userDto = userService.getCurrentUserinfo();
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/user/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        myPageService.updateUser(userDto);
        securityService.updateSecurityContext(userDto.getUsername());

        return ResponseEntity.ok(userDto);
    }
}
