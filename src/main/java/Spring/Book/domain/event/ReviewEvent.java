package Spring.Book.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReviewEvent extends ApplicationEvent {

    private final String notificationMessage;
    private final Long userId;

    public ReviewEvent(Object source, String notificationMessage, Long userId){
        super(source);
        this.notificationMessage = notificationMessage;
        this.userId = userId;
    }
}
