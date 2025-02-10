package Spring.Book.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserStatusCount {
    private Long activeCount;
    private Long inactiveCount;
}