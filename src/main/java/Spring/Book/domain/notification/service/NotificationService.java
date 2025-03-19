package Spring.Book.domain.notification.service;

import Spring.Book.domain.notification.dto.NotificationDto;
import Spring.Book.domain.notification.entity.notificationEntity;
import Spring.Book.domain.notification.repository.NotificationRepository;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void notificationEvent(List<Long> adminIds, String notificationMessage) {
        List<UserEntity> admins = userRepository.findAllById(adminIds);

        if (admins.isEmpty()) {
            throw new IllegalArgumentException("관리자들을 찾을 수 없음");
        }

        List<notificationEntity> notifications = new ArrayList<>();
        for (UserEntity admin : admins) {
            notificationEntity notification = notificationEntity.builder()
                    .user(admin)
                    .isRead(false)
                    .message(notificationMessage)
                    .build();
            notifications.add(notification);
        }

        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void RegistrationEvent(Long userId, String notificationMessage) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("신규화원을 찾을 수 없습니다."));

        user.updateMileage(3000);

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
        notifications.forEach(notificationEntity::markAsRead);
        notificationRepository.saveAll(notifications);
    }

    public boolean hasUnreadNotifications(Long userId) {
        return notificationRepository.existsByUserIdAndIsReadFalse(userId);
    }

    public List<NotificationDto> getAllNotificationsByUserId(Long userId) {
        List<notificationEntity> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream()
                .map(notification -> new NotificationDto(notification.getMessage(), notification.isRead()))
                .collect(Collectors.toList());
    }
}
