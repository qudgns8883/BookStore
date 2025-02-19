package Spring.Book.domain.notification.controller;

import Spring.Book.domain.notification.dto.NotificationDto;
import Spring.Book.domain.notification.service.NotificationService;
import Spring.Book.domain.notification.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class SseNotificationController{

    private final SseService sseService;
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam Long userId) {
        return sseService.subscribe(userId);
    }

    @PutMapping("/read")
    public ResponseEntity<Void> markAllAsRead(@RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread")
    public ResponseEntity<Boolean> hasUnreadNotifications(@RequestParam Long userId) {
        boolean hasUnread = notificationService.hasUnreadNotifications(userId);
        return ResponseEntity.ok(hasUnread);
    }

    @GetMapping("/list")
    public ResponseEntity<List<NotificationDto>> getNotifications(@RequestParam Long userId) {
        List<NotificationDto> notifications = notificationService.getAllNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }
}
