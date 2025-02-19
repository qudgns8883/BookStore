package Spring.global.listener;

import Spring.global.event.UserRegisteredEvent;
import Spring.Book.domain.notification.service.NotificationService;
import Spring.Book.domain.notification.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegistrationEventListener {

    private final NotificationService notificationService;
    private final SseService sseService;

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        notificationService.RegistrationEvent(event.getUserId(), event.getRegisteredMessage());
        sseService.sendMessageToUser(event.getRegisteredMessage());
    }
}