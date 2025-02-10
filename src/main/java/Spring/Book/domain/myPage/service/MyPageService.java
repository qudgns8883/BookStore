package Spring.Book.domain.myPage.service;

import Spring.Book.domain.user.dto.UserDto;
import Spring.Book.domain.user.entity.UserEntity;
import Spring.Book.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    @Transactional
    public void updateUser(UserDto userDto) {
        UserEntity existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        UserEntity updatedUser = UserEntity.builder()
                .id(existingUser.getId())
                .nickname(userDto.getNickname())
                .username(userDto.getUsername())
                .password(existingUser.getPassword())
                .birthdate(userDto.getBirthdate())
                .mileage(existingUser.getMileage())
                .role(existingUser.getRole())
                .address(userDto.getAddress())
                .products(existingUser.getProducts())
                .carts(existingUser.getCarts())
                .payments(existingUser.getPayments())
                .orders(existingUser.getOrders())
                .reviews(existingUser.getReviews())
                .build();

        userRepository.save(updatedUser);
    }
}
