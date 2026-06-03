package kr.co.shop.repository.jpa;

import kr.co.shop.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 컨텐츠 Repository
 */
public interface ContentRepository extends JpaRepository<Content, Long> {
}