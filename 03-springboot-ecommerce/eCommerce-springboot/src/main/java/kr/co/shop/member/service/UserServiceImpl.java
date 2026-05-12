package kr.co.shop.member.service;

import kr.co.shop.member.dto.UserDto;
import kr.co.shop.member.entity.User;
import kr.co.shop.member.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User login(String email, String password) {
        System.out.println("서비스 이메일+ " + email);
        System.out.println("서비스 비밀번호 " + password);

        User user = userMapper.findByEmail(email);

        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            return null;
        }

        return user;
    }

    @Override
    @Transactional
    public Long join(UserDto userDto) {
        validateDuplicateUser(userDto);
        String encodedPassword = passwordEncoder.encode(userDto.getUserPassword());
        User user = userDto.toEntity(encodedPassword);

        User savedUser = userRepository.save(user);

        log.info("회원가입 완료: userNo={}, userEmail={}",
                savedUser.getUserNo(),
                savedUser.getUserEmail());

        System.out.println("확인코드 레파지토리 -> " + userDto);
        return savedUser.getUserNo();
    }

    private void validateDuplicateUser(UserDto userDto) {
        if (userRepository.existsByUserId(userDto.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (userRepository.existsByUserEmail(userDto.getUserEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }
}