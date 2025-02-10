package Spring.Book.domain.user.controller;

import Spring.Book.domain.user.dto.FindEmailDto;
import Spring.Book.domain.user.dto.UserDto;
import Spring.Book.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(){

        return "login";
    }

    @PostMapping("/user/joinProc")
    public String register(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }
        userService.register(userDto);

        return "redirect:/user/signup";
    }

    @GetMapping("/user/signup")
    public String signup(@ModelAttribute("userDto") UserDto userDto, Model model) {

        UserDto updatedUserDto = userDto.toBuilder()
                .mileage(userDto.getMileage() != null ? userDto.getMileage() : 1000)
                .build();

        model.addAttribute("userDto", updatedUserDto);

        return "user/signup";
    }

    @GetMapping("/user/checkNickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean isAvailable = userService.isNicknameTaken(nickname);
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping("/user/findUsername")
    @ResponseBody
    public ResponseEntity<?> findUsername(@Valid @RequestBody FindEmailDto findEmailDto) {

        Optional<UserDto> email = userService.findEmail(findEmailDto);

        if (email.isPresent()) {
            return ResponseEntity.ok(email);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가입한 아이디가 없습니다.");
        }
    }

}
