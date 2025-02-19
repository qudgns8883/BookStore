package Spring.global.listener;

import Spring.global.event.ReviewEvent;
import Spring.Book.domain.notification.service.NotificationService;
import Spring.Book.domain.notification.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewRegistration {

    private final NotificationService notificationService;
    private final SseService sseService;

    @EventListener
    public void handleReviewEvent(ReviewEvent event) {

        notificationService.notificationEvent(event.getUserId(), event.getNotificationMessage());
        System.out.println("🚀 SSE 알림 전송 시작...");
        sseService.sendMessageToUser(event.getNotificationMessage());
    }
}
