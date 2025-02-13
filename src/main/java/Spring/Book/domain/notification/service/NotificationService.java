package Spring.Book.domain.notification.service;

import Spring.Book.domain.notification.dto.PurchaseEntityDto;
import Spring.Book.domain.notification.entity.notificationEntity;
import Spring.Book.domain.notification.repository.NotificationRepository;
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
    public void notificationEvent(Long adminId, String notificationMessage) {
        UserEntity admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없음"));

        notificationEntity notification = notificationEntity.builder()
                .user(admin)
                .isRead(false)
                .message(notificationMessage)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void RegistrationEvent(Long userId, String notificationMessage) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("신규화원을 찾을 수 없습니다."));

        int currentMileage = (user.getMileage() != null) ? user.getMileage() : 0;
        user.setMileage(currentMileage + 3000);

        notificationEntity notification = notificationEntity.builder()
                .user(user)
                .isRead(false)
                .message(notificationMessage)
                .build();

        notificationRepository.save(notification);

    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<notificationEntity> notifications = notificationRepository.findByUserId(userId);
        notifications.forEach(notification -> {
            notification.setRead(true);
        });
        notificationRepository.saveAll(notifications);
    }

    public boolean hasUnreadNotifications(Long userId) {
        return notificationRepository.existsByUserIdAndIsReadFalse(userId);
    }

    public List<PurchaseEntityDto> getAllNotificationsByUserId(Long userId) {
        List<notificationEntity> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream()
                .map(notification -> new PurchaseEntityDto(notification.getMessage(), notification.isRead()))
                .collect(Collectors.toList());
    }
}
