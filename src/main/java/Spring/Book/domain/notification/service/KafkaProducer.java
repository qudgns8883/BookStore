package Spring.Book.domain.notification.service;

import Spring.Book.domain.notification.dto.KafkaMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, KafkaMessageDto> purchasekafkaTemplate;
    private final KafkaTemplate<String, String> logKafkaTemplate;

    public void sendNotification(KafkaMessageDto kafkaMessage) {

        purchasekafkaTemplate.send("purchase-notification", kafkaMessage);
    }

    public void sendLog(String logMessage) {
        logKafkaTemplate.send("log-topic", logMessage);
    }
}
