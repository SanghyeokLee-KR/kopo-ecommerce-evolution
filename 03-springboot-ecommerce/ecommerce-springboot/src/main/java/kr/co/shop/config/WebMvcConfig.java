package kr.co.shop.config;

import kr.co.shop.common.interceptor.AdminCheckInterceptor;
import kr.co.shop.common.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 인터셉터 설정
 * TODO : 스프링 시큐리티 사용시 삭제 예정
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 로그인한 사용자만 접근 가능한 페이지
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns(
                        // 회원 관련 (로그인, 회원가입 제외)
                        "/users/**",

                        // 장바구니
                        "/basket/**",

                        // 주문
                        "/orders/**"
                )
                .excludePathPatterns(
                        // 비회원도 접근 가능한 회원 페이지
                        "/users/login",
                        "/users/signup"
                );

        // 관리자 권한이 필요한 페이지
        registry.addInterceptor(new AdminCheckInterceptor())
                .addPathPatterns("/admin/**");
    }
}