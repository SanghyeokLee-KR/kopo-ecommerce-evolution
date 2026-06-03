package kr.co.shop.dto.basket.response;

import java.util.List;

/**
 * 장바구니 응답 DTO
 */
public record BasketResponse(
        Long nbBasket,
        List<BasketItemResponse> items,
        Integer totalAmount
) {
}