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
public class KafkaNotification {

    private final SseService sseService;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @org.springframework.kafka.annotation.KafkaListener(topics = "purchase-notification", groupId = "notification-group")
    public void receiveNotification(String message) {
        try {

            KafkaMessageDto kafkaMessage = objectMapper.readValue(message, KafkaMessageDto.class);
            sseService.sendMessageToUser(kafkaMessage.getMessage());
            notificationService.notificationEvent(kafkaMessage.getUserId(), kafkaMessage.getMessage());

        } catch (JsonProcessingException e) {
            log.error("Kafka 메시지 역직렬화 실패: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Kafka 처리 실패: {}", e.getMessage(), e);
        }
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "log-topic", groupId = "log-group")
    public void receiveLog(String logMessage) {
    }
}
