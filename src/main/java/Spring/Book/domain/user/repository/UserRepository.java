package Spring.Book.domain.user.repository;

import Spring.Book.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUsername(String username);
    Boolean existsByNickname(String nickname);
    UserEntity findByUsername(String username);
    Optional<UserEntity> findByNicknameAndBirthdate(String nickname, String birthdate);
    Optional<UserEntity> findByNickname(String nickname);
}
