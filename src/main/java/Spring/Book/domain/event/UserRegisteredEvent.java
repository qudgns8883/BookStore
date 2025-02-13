package Spring.Book.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {

    private final String RegisteredMessage;
    private final Long userId;

    public UserRegisteredEvent(Object source, String notificationMessage, Long userId) {
        super(source);
        this.RegisteredMessage = notificationMessage;
        this.userId = userId;
    }
}