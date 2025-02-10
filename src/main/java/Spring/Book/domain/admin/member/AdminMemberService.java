package Spring.Book.domain.admin.member;

import Spring.Book.domain.user.dto.UserDto;
import Spring.Book.domain.user.dto.UserStatusCount;
import Spring.Book.domain.user.entity.Status;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final UserRepository userRepository;

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

    public UserStatusCount countUserStatus() {
        return userRepository.countUserStatus();
    }

    public List<UserDto> searchMembers(String status, String searchBy, String query) {

        log.debug("Searching members with status: {}, searchBy: {}, query: {}", status, searchBy, query);

        Status memberStatus = status.equals("ACTIVE") ? Status.ACTIVE : Status.INACTIVE;
        List<UserEntity> users = new ArrayList<>();

        if ("username".equals(searchBy)) {
            users = userRepository.findByUsernameAndStatus(query, memberStatus);
        } else if ("nickname".equals(searchBy)) {
            users = userRepository.findByNicknameAndStatus(query, memberStatus);
        }

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