package Spring.global.config;

import Spring.Book.domain.notification.dto.KafkaMessageDto;
import Spring.Book.domain.notification.service.NotificationService;
import Spring.Book.domain.notification.service.SseService;
import Spring.Book.domain.product.extractor.PurchaseLogParameterExtractor;
import Spring.global.aspect.LogParameterExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfig {

//    private final SseService sseService;
//    private final NotificationService notificationService;
//    private final ObjectMapper objectMapper;
//    private final PurchaseLogParameterExtractor purchaseLogParameterExtractor;


    // Kafka Producer 설정
    @Bean // KafkaTemplate을 스프링 빈으로 등록하여 다른 곳에서 사용 가능하도록 설정
    public KafkaTemplate<String, KafkaMessageDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory()); // ProducerFactory를 통해 KafkaTemplate 생성
    }

    // Kafka Producer 설정
    @Bean
    public KafkaTemplate<String, String> logKafkaTemplate() {
        return new KafkaTemplate<>(producerFactoryString()); // String으로 직렬화할 경우
    }

    // Kafka Producer의 설정을 담고 있는 맵 반환
    private Map<String, Object> producerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka 서버 주소
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // Key는 StringSerializer로 설정
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // Value는 JsonSerializer로 변경
        return props;
    }

    // Kafka Producer 설정 (String용)
    private Map<String, Object> producerPropsString() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // String은 StringSerializer로 직렬화
        return props;
    }

    // Kafka Producer를 생성하는 Factory
    private DefaultKafkaProducerFactory<String, KafkaMessageDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProps()); // Producer 설정을 적용하여 생성
    }

    // Kafka Consumer 설정
    @Bean // Kafka ConsumerFactory를 스프링 빈으로 등록
    public ConsumerFactory<String, KafkaMessageDto> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka 서버 주소
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group"); // 컨슈머 그룹 ID 설정

        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        // JsonDeserializer에 대한 설정
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "Spring.Book.domain.notification.dto");
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, KafkaMessageDto.class.getName()); // 처리할 클래스 타입 지정
        return new DefaultKafkaConsumerFactory<>(configProps);// 설정을 적용한 ConsumerFactory 반환
    }

    private DefaultKafkaProducerFactory<String, String> producerFactoryString() {
        return new DefaultKafkaProducerFactory<>(producerPropsString());
    }

}