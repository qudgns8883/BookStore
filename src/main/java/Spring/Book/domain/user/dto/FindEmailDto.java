package Spring.Book.domain.user.dto;

import Spring.Book.domain.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindEmailDto {

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;
    private String birthdate;

}

