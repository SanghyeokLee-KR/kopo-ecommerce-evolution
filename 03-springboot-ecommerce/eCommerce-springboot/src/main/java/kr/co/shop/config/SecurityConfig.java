package kr.co.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 개발 초기 단계에서는 모든 요청을 허용한다.
@Configuration
public class SecurityConfig {

    // 모든 URL에 대해 인증 없이 접근 가능하도록 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                // 기본 로그인 화면 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // 로그아웃 기능 비활성화
                .logout(AbstractHttpConfigurer::disable)
                // 개발 편의를 위해 CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // 회원 비밀번호 암호화에 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}