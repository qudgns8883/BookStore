package Spring.Book.domain.user.repository;

import Spring.Book.domain.user.dto.UserStatusCount;
import Spring.Book.domain.user.entity.Role;
import Spring.Book.domain.user.entity.Status;
import Spring.Book.domain.user.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUsername(String username);
    Boolean existsByNickname(String nickname);
    UserEntity findByUsername(String username);
    Optional<UserEntity> findByNicknameAndBirthdate(String nickname, String birthdate);
    Optional<UserEntity> findByNickname(String nickname);
    @Query("SELECT new Spring.Book.domain.user.dto.UserStatusCount(" +
            "COUNT(CASE WHEN u.status = 'ACTIVE' THEN 1 END), " +
            "COUNT(CASE WHEN u.status = 'INACTIVE' THEN 1 END)) " +
            "FROM UserEntity u")
    UserStatusCount countUserStatus();
    List<UserEntity> findByUsernameAndStatus(String username, Status status);
    List<UserEntity> findByNicknameAndStatus(String nickname, Status status);
    List<UserEntity> findByRole(Role role);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserEntity u WHERE u.nickname = :nickname")
    Optional<UserEntity> findByNicknameWithLock(@Param("nickname") String nickname);
}
