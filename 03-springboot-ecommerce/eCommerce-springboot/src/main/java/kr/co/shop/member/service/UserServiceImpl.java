package kr.co.shop.member.service;

import kr.co.shop.member.dto.request.UserJoinRequest;
import kr.co.shop.member.entity.User;
import kr.co.shop.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long join(UserJoinRequest request) {
        validateDuplicateUser(request);

        String encodedPassword = passwordEncoder.encode(request.getUserPassword());
        User user = request.toEntity(encodedPassword);

        User savedUser = userRepository.save(user);

        log.info("회원가입 완료: userNo={}, userEmail={}",
                savedUser.getUserNo(),
                savedUser.getUserEmail());

        return savedUser.getUserNo();
    }

    private void validateDuplicateUser(UserJoinRequest request) {
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }
}