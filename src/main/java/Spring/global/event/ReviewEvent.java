package Spring.global.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class ReviewEvent extends ApplicationEvent {

    private final String notificationMessage;
    private final List<Long> userId;

    public ReviewEvent(Object source, String notificationMessage, List<Long> userId){
        super(source);
        this.notificationMessage = notificationMessage;
        this.userId = userId;
    }
}
