package kr.co.shop.service;

import jakarta.validation.constraints.Min;
import kr.co.shop.dto.basket.response.BasketResponse;

/**
 * 장바구니 서비스
 */
public interface BasketService {

    /**
     * 장바구니에 상품 추가
     */
    void addItem(
            Long nbUser,
            Long nbProduct,
            @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
            int quantity
    );

    /**
     * 장바구니 조회
     */
    BasketResponse getBasket(Long nbUser);

    /**
     * 장바구니 상품 수량 변경
     */
    void updateQuantity(
            Long nbUser,
            Long nbBasketItem,
            @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
            int quantity
    );

    /**
     * 장바구니 상품 삭제
     */
    void removeItem(Long nbUser, Long nbBasketItem);

    /**
     * 장바구니 전체 비우기
     */
    void clearBasket(Long nbUser);
}