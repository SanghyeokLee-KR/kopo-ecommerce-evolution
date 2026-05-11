package kr.co.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// JPA Auditing 기능을 사용하기 위한 설정 클래스
// @CreatedDate, @LastModifiedDate가 동작하도록 설정
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}