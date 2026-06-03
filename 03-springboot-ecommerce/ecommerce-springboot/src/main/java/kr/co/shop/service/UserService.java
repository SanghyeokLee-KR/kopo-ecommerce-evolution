package kr.co.shop.service;

import kr.co.shop.dto.user.request.LoginRequest;
import kr.co.shop.dto.user.request.MyPageUpdateRequest;
import kr.co.shop.dto.user.request.PasswordChangeRequest;
import kr.co.shop.dto.user.request.UserSignupRequest;
import kr.co.shop.dto.user.response.MyPageResponse;
import kr.co.shop.dto.user.response.UserSessionResponse;

/**
 * 사용자 서비스
 */
public interface UserService {

    /**
     * 회원가입
     */
    void signup(UserSignupRequest request);

    /**
     * 로그인
     */
    UserSessionResponse login(LoginRequest request);

    /**
     * 마이페이지 조회
     */
    MyPageResponse getMyPage(Long nbUser);

    /**
     * 마이페이지 수정
     */
    void updateMyPage(Long nbUser, MyPageUpdateRequest request);

    /**
     * 비밀번호 변경
     */
    void changePassword(Long nbUser, PasswordChangeRequest request);

    /**
     * 회원 탈퇴 요청
     */
    void requestWithdraw(Long nbUser);
}