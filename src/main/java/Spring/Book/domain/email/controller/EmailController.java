package Spring.Book.domain.email.controller;

import Spring.Book.domain.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;


    @PostMapping("/email/sendVerificationCode")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String username) {
        try {
            emailService.sendVerificationCode(username);

            return ResponseEntity.ok("인증번호가 전송되었습니다. 이메일을 확인해주세요.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("유효하지 않은 이메일 주소입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버에서 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    @PostMapping("/email/verifyCode")
    public ResponseEntity<Map<String, String>> verifyCode(@RequestParam String verificationCode, @RequestParam String username) {
        boolean isValid = emailService.verifyCode(username, verificationCode);
        Map<String, String> response = new HashMap<>();
        if (isValid) {
            response.put("message", "인증번호가 확인되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "인증번호가 일치하지 않습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}

