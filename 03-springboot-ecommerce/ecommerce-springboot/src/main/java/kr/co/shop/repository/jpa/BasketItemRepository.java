package kr.co.shop.repository.jpa;

import kr.co.shop.domain.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 장바구니 품목 Repository
 */
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

    // 주문 생성 시 장바구니 품목 전체 조회
    List<BasketItem> findByBasket_NbBasketOrderByCnBasketItemOrderAsc(Long nbBasket);

    // 같은 상품이 이미 장바구니에 있는지 조회
    Optional<BasketItem> findByBasket_NbBasketAndProduct_NbProduct(
            Long nbBasket,
            Long nbProduct
    );

    // 장바구니 품목 개수 조회
    long countByBasket_NbBasket(Long nbBasket);

    // 장바구니 전체 비우기
    void deleteByBasket_NbBasket(Long nbBasket);
}