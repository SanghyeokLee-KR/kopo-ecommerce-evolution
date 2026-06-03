package kr.co.shop.repository.jpa;

import kr.co.shop.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문 품목 Repository
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}