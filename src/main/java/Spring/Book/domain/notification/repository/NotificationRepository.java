package Spring.Book.domain.notification.repository;

import Spring.Book.domain.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUserId(Long userId);
    boolean existsByUserIdAndIsReadFalse(Long userId);
}
