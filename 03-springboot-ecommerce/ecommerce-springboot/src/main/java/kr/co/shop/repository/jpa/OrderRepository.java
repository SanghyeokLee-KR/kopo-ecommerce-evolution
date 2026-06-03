package kr.co.shop.repository.jpa;

import kr.co.shop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문 Repository
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}