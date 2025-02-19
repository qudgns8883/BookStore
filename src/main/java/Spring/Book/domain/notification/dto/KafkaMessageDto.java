package Spring.Book.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KafkaMessageDto {

    private String message;
    private List<Long> userId;

    @Builder
    public KafkaMessageDto(String message, List<Long> userId) {
        this.message = message;
        this.userId = userId;
    }
}
