package Spring.Book.domain.event;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PurchaseEvent extends ApplicationEvent {

    private final String notificationMessage;
    private final Long adminId;

    public PurchaseEvent(Object source, String notificationMessage, Long adminId){
        super(source);
        this.notificationMessage = notificationMessage;
        this.adminId = adminId;
    }
}
