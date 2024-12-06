package Spring.Book.domain.user.dto;

import Spring.Book.domain.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
public class UserDto {

    private Long id;
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;

    @Email(message = "유효한 이메일 주소를 입력해야 합니다.")
    @NotBlank(message = "이메일을 입력해주세요")
    private String username;
    private String birthdate;
    private Integer mileage;
    private Role role;
    private Address address;
    private String createDate;

    private UserDto(Long id, String nickname, String password, String username,
                    String birthdate,Integer mileage, Role role, Address address, String createDate) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.username = username;
        this.birthdate = birthdate;
        this.mileage = mileage;
        this.role = role;
        this.address = address;
        this.createDate = createDate;
    }
}

