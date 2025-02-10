package Spring.Book.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
