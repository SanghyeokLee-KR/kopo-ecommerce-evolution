package kr.co.shop.common.util;

import jakarta.servlet.http.HttpSession;
import kr.co.shop.dto.user.response.UserSessionResponse;

/**
 * 세션 처리 유틸
 */
public class SessionUtil {

    public static final String LOGIN_USER = "loginUser";

    private SessionUtil() {
    }

    // 로그인 사용자 조회
    public static UserSessionResponse getLoginUser(HttpSession session) {
        return (UserSessionResponse) session.getAttribute(LOGIN_USER);
    }

    // 로그인 여부 확인
    public static boolean isLogin(HttpSession session) {
        return getLoginUser(session) != null;
    }

    // 로그인 사용자 저장
    public static void setLoginUser(
            HttpSession session,
            UserSessionResponse loginUser
    ) {
        session.setAttribute(LOGIN_USER, loginUser);
    }

    // 로그아웃
    public static void logout(HttpSession session) {
        session.invalidate();
    }
}