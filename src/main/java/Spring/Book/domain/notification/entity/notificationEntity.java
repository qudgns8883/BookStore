package Spring.Book.domain.notification.entity;

import Spring.Book.domain.user.entity.UserEntity;
import Spring.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notification")
public class notificationEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public notificationEntity(Long id, String message, boolean isRead, UserEntity user){
        this.id = id;
        this.message = message;
        this.isRead = isRead;
        this.user = user;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
