package kr.co.shop.repository.jpa;

import kr.co.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자 Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // 아이디로 조회
    Optional<User> findByIdUser(String idUser);

    // 아이디 중복 확인
    boolean existsByIdUser(String idUser);

    // 이메일 중복 확인
    boolean existsByNmEmail(String nmEmail);
}