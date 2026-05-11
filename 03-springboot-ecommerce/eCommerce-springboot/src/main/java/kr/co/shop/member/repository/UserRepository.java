package kr.co.shop.member.repository;

import kr.co.shop.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 로그인할 때 이메일로 회원을 찾기 위해 사용
    Optional<User> findByUserEmail(String userEmail);

    // 회원가입 시 아이디 중복 확인
    boolean existsByUserId(String userId);

    // 회원가입 시 이메일 중복 확인
    boolean existsByUserEmail(String userEmail);
}