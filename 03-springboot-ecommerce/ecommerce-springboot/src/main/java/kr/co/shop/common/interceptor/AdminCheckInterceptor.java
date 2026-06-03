package kr.co.shop.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.shop.common.enums.UserType;
import kr.co.shop.common.util.SessionUtil;
import kr.co.shop.dto.user.response.UserSessionResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 관리자 권한 확인
 */
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        HttpSession session = request.getSession(false);

        // 로그인 여부 확인
        if (session == null || !SessionUtil.isLogin(session)) {
            response.sendRedirect("/users/login");
            return false;
        }

        UserSessionResponse loginUser = SessionUtil.getLoginUser(session);

        // 관리자만 접근 가능
        if (!UserType.ADMIN.getCode().equals(loginUser.cdUserType())) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}