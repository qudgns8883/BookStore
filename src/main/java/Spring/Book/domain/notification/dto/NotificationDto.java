package Spring.Book.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationDto {

    private String message;
    private boolean isRead;
    @Builder
    public NotificationDto(String message, boolean isRead) {
        this.message = message;
        this.isRead = isRead;
    }
}
