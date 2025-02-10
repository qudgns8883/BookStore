package Spring.global.handler;

import Spring.Book.domain.event.PurchaseEvent;
import Spring.Book.domain.notification.service.NotificationService;
import Spring.Book.domain.notification.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final SseService sseService;

    @EventListener
    public void handlePurchaseEvent(PurchaseEvent event) {

        notificationService.createNotification(event.getAdminId(), event.getNotificationMessage());

        sseService.sendNotificationToAllAdmins(event.getNotificationMessage());
    }
}
