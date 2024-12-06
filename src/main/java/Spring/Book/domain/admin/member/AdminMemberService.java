package Spring.Book.domain.admin.member;


import Spring.Book.domain.user.dto.UserDto;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final UserRepository userRepository;

    // 모든 회원 조회
    public List<UserDto> findall() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .address(user.getAddress())
                        .birthdate(user.getBirthdate())
                        .mileage(user.getMileage())
                        .createDate(user.getCreateDateAsString())
                        .build())
                .collect(Collectors.toList());
    }
}
