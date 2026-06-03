package kr.co.shop.service;

import kr.co.shop.common.enums.UserStatus;
import kr.co.shop.common.enums.UserType;
import kr.co.shop.common.exception.BusinessException;
import kr.co.shop.common.exception.ErrorCode;
import kr.co.shop.domain.User;
import kr.co.shop.dto.user.request.LoginRequest;
import kr.co.shop.dto.user.request.MyPageUpdateRequest;
import kr.co.shop.dto.user.request.PasswordChangeRequest;
import kr.co.shop.dto.user.request.UserSignupRequest;
import kr.co.shop.dto.user.response.MyPageResponse;
import kr.co.shop.dto.user.response.UserSessionResponse;
import kr.co.shop.repository.jpa.UserRepository;
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
    public void signup(UserSignupRequest request) {
        if (userRepository.existsByIdUser(request.getIdUser())) {
            throw new BusinessException(ErrorCode.DUPLICATE_USER_ID);
        }

        if (userRepository.existsByNmEmail(request.getNmEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getNmPaswd());

        User user = User.builder()
                .idUser(request.getIdUser())
                .nmUser(request.getNmUser())
                .nmPaswd(encodedPassword)
                .noMobile(request.getNoMobile())
                .nmEmail(request.getNmEmail())
                // 가입 후 (가입요청) 사용하면 안 됨
                // 관리자 승인 후 -> 정상으로 바뀌는 구조
                .stStatus(UserStatus.JOIN_REQUEST.getCode())
                .cdUserType(UserType.USER.getCode())
                .build();

        userRepository.save(user);

        log.info("회원가입 완료 - idUser={}", user.getIdUser());
    }

    @Override
    public UserSessionResponse login(LoginRequest request) {
        // TODO: 스프링 시큐리티 적용 시 삭제 예정 (AuthenticationManager)
        User user = userRepository.findByIdUser(request.getIdUser())
                .orElseThrow(() -> {
                    log.warn("로그인 실패 - 존재하지 않는 아이디: idUser={}", request.getIdUser());
                    return new BusinessException(ErrorCode.USER_NOT_FOUND);
                });

        if (!passwordEncoder.matches(request.getNmPaswd(), user.getNmPaswd())) {
            log.warn("로그인 실패 - 비밀번호 불일치: idUser={}", request.getIdUser());
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        // JOIN_REQUEST, WITHDRAW_REQUEST 등 NORMAL이 아닌 상태는 전부 막음
        if (!UserStatus.NORMAL.getCode().equals(user.getStStatus())) {
            log.warn("로그인 실패 - 미승인 계정: idUser={}, stStatus={}", request.getIdUser(), user.getStStatus());
            throw new BusinessException(ErrorCode.NOT_APPROVED_USER);
        }

        log.info("로그인 성공 - nbUser={}, idUser={}", user.getNbUser(), user.getIdUser());

        return new UserSessionResponse(
                user.getNbUser(),
                user.getIdUser(),
                user.getNmUser(),
                user.getNmEmail(),
                user.getStStatus(),
                user.getCdUserType()
        );
    }

    @Override
    public MyPageResponse getMyPage(Long nbUser) {
        User user = userRepository.findById(nbUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new MyPageResponse(
                user.getIdUser(),
                user.getNmUser(),
                user.getNoMobile(),
                user.getNmEmail()
        );
    }

    @Override
    @Transactional
    public void updateMyPage(Long nbUser, MyPageUpdateRequest request) {
        User user = userRepository.findById(nbUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 이메일이 변경되는 경우에만 중복 체크. 같은 이메일로 저장하는 건 허용
        if (!user.getNmEmail().equals(request.getNmEmail())
                && userRepository.existsByNmEmail(request.getNmEmail())) {
            log.warn("이메일 중복 - nbUser={}, 요청 이메일={}", nbUser, request.getNmEmail());
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        user.updateUserInfo(
                request.getNmUser(),
                request.getNoMobile(),
                request.getNmEmail()
        );

        log.info("마이페이지 수정 완료 - nbUser={}", nbUser);
    }

    @Override
    @Transactional
    public void changePassword(Long nbUser, PasswordChangeRequest request) {
        User user = userRepository.findById(nbUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getNmPaswd())) {
            log.warn("비밀번호 변경 실패 - 현재 비밀번호 불일치: nbUser={}", nbUser);
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));

        log.info("비밀번호 변경 완료 - nbUser={}", nbUser);
    }

    @Override
    @Transactional
    public void requestWithdraw(Long nbUser) {
        User user = userRepository.findById(nbUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // FIXME: 나중에 스케줄러 붙여서 자동화 필요
        user.changeStatus(UserStatus.WITHDRAW_REQUEST.getCode());

        log.info("탈퇴 요청 완료 - nbUser={}", nbUser);
    }
}