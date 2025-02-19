package Spring.global.listener;

import Spring.Book.domain.notification.dto.KafkaMessageDto;
import Spring.Book.domain.notification.service.NotificationService;
import Spring.Book.domain.notification.service.SseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseNotification {

    private final SseService sseService;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @org.springframework.kafka.annotation.KafkaListener(topics = "purchase-notification", groupId = "notification-group")
    public void receiveNotification(String message) {
        try {
            System.out.println("Received message: " + message);
            // 메시지를 객체로 변환
            KafkaMessageDto kafkaMessage = objectMapper.readValue(message, KafkaMessageDto.class);

            // 메시지를 SSE를 통해 관리자에게 전달
            sseService.sendMessageToUser(kafkaMessage.getMessage());

            // 알림 이벤트 처리
            notificationService.notificationEvent(kafkaMessage.getUserId(), kafkaMessage.getMessage());

        } catch (JsonProcessingException e) {
            log.error("Kafka 메시지 역직렬화 실패: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Kafka 처리 실패: {}", e.getMessage(), e);
        }
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "log-topic", groupId = "log-group")
    public void receiveLog(String logMessage) {
        System.out.println("Received log: " + logMessage);
        // 로그를 파일 또는 ElasticSearch 같은 로그 분석 시스템으로 저장하는 로직 추가 가능
    }
}
