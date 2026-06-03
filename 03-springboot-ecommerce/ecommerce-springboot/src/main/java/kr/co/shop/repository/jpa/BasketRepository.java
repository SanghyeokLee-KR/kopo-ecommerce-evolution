package kr.co.shop.repository.jpa;

import kr.co.shop.domain.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 장바구니 Repository
 */
public interface BasketRepository extends JpaRepository<Basket, Long> {

    // 사용자 번호로 장바구니 조회
    Optional<Basket> findByUser_NbUser(Long nbUser);
}