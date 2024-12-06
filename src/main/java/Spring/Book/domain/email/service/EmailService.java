package Spring.Book.domain.email.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;

    // 인증번호 생성 및 이메일 전송
    public void sendVerificationCode(String username) throws MessagingException {
        if (!isValidEmail(username)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }

        String verificationCode = generateVerificationCode();

        // 이메일 본문 생성
        String mailContent = ""; // 새로운 변수에 내용을 담음
        mailContent += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
        mailContent += "<h1>" + verificationCode + "</h1>";
        mailContent += "<h3>" + "감사합니다." + "</h3>";

        System.out.println("이메일로 전송된 인증번호: " + verificationCode);

        // 이메일 전송 설정
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("qudgns8882@naver.com");
        helper.setTo(username);
        helper.setSubject("이메일 인증");
        helper.setText(mailContent, true); // HTML 형식으로 전송

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        valueOps.set(username, verificationCode, 3, TimeUnit.MINUTES);

        // 이메일 전송
        mailSender.send(mimeMessage);
    }

    // 이메일 형식 검증
    private boolean isValidEmail(String username) {
        return username != null && username.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    // 인증번호 생성
    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000 + 100000)); // 6자리 랜덤 숫자
    }

    // 인증번호 검증 메서드 (boolean 반환)
    public boolean verifyCode(String username, String code) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String storedCode = valueOps.get(username); // Redis에서 이메일로 저장된 인증번호 가져오기

        // 저장된 인증번호와 입력된 인증번호를 비교
        return storedCode != null && storedCode.equals(code);
    }
}
