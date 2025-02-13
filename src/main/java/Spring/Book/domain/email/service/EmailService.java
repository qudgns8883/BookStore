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

    public void sendVerificationCode(String username) throws MessagingException {
        if (!isValidEmail(username)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }

        String verificationCode = generateVerificationCode();

        System.out.println("인증번호" + verificationCode);

        String mailContent = "";
        mailContent += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
        mailContent += "<h1>" + verificationCode + "</h1>";
        mailContent += "<h3>" + "감사합니다." + "</h3>";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("qudgns8882@naver.com");
        helper.setTo(username);
        helper.setSubject("이메일 인증");
        helper.setText(mailContent, true);

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        valueOps.set(username, verificationCode, 3, TimeUnit.MINUTES);


        mailSender.send(mimeMessage);
    }

    private boolean isValidEmail(String username) {
        return username != null && username.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000 + 100000));
    }

    public boolean verifyCode(String username, String code) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String storedCode = valueOps.get(username);

        return storedCode != null && storedCode.equals(code);
    }
}
