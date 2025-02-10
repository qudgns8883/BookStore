package Spring.Book.domain.user.service;

import Spring.Book.domain.user.dto.Address;
import Spring.Book.domain.user.dto.FindEmailDto;
import Spring.Book.domain.user.dto.UserDto;
import Spring.Book.domain.user.entity.Role;
import Spring.Book.domain.user.entity.Status;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void register(UserDto userDto){

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new DataIntegrityViolationException("이미 존재하는 이메일입니다.");
        }

        if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new DataIntegrityViolationException("이미 존재하는 닉네임입니다.");
        }

        UserEntity user = UserEntity.builder()
                .nickname(userDto.getNickname())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .username(userDto.getUsername())
                .birthdate(userDto.getBirthdate())
                .mileage(userDto.getMileage())
                .role(Role.ADMIN)
                .status(Status.ACTIVE)
                .address(userDto.getAddress())
                .build();

        userRepository.save(user);
    }

    public boolean isNicknameTaken(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public Optional<UserDto> findEmail(FindEmailDto findEmailDto) {

        UserEntity userEntity = UserEntity.builder()
                .nickname(findEmailDto.getNickname())
                .birthdate(findEmailDto.getBirthdate())
                .build();

        Optional<UserEntity> foundUser = userRepository.findByNicknameAndBirthdate(userEntity.getNickname(), userEntity.getBirthdate());

        return foundUser.map(user -> UserDto.builder()
                .username(user.getUsername())
                .build());

    }

    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    public UserDto getCurrentUserinfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return UserDto.builder()
                .id(userEntity.getId())
                .nickname(userEntity.getNickname())
                .username(userEntity.getUsername())
                .mileage(userEntity.getMileage())
                .birthdate(userEntity.getBirthdate())
                .address(userEntity.getAddress())
                .build();
    }
}

