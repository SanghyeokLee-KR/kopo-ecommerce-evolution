package kr.co.shop.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.shop.common.util.SessionUtil;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 로그인 여부 확인
 */
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        HttpSession session = request.getSession(false);

        // 세션이 없거나 로그인 정보가 없으면 로그인 페이지로 이동
        if (session == null || !SessionUtil.isLogin(session)) {
            response.sendRedirect("/users/login");
            return false;
        }

        return true;
    }
}