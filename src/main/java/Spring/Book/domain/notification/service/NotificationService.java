package Spring.Book.domain.notification.service;

import Spring.Book.domain.notification.dto.NotificationDto;
import Spring.Book.domain.notification.entity.NotificationEntity;
import Spring.Book.domain.notification.repository.NotificationRepository;
import Spring.Book.domain.order.repository.OrderRepository;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void createNotification(Long adminId, String notificationMessage) {
        UserEntity admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없음"));

        NotificationEntity notification = NotificationEntity.builder()
                .user(admin)
                .isRead(false)
                .message(notificationMessage)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<NotificationEntity> notifications = notificationRepository.findByUserId(userId);
        notifications.forEach(notification -> {
            notification.setRead(true);
        });
        notificationRepository.saveAll(notifications);
    }

    public boolean hasUnreadNotifications(Long userId) {
        return notificationRepository.existsByUserIdAndIsReadFalse(userId);
    }

    public List<NotificationDto> getAllNotificationsByUserId(Long userId) {
        List<NotificationEntity> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream()
                .map(notification -> new NotificationDto(notification.getMessage(), notification.isRead()))
                .collect(Collectors.toList());
    }
}
