package kr.co.shop.dto.basket.response;

/**
 * 장바구니 품목 응답 DTO
 */
public record BasketItemResponse(
        Long nbBasketItem,
        Long nbProduct,
        String nmProduct,
        String imagePath,
        Integer unitPrice,
        Integer quantity,
        Integer amount
) {
}