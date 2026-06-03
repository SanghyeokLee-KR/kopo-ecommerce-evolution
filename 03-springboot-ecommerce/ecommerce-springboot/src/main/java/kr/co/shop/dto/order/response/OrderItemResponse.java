package kr.co.shop.dto.order.response;

/**
 * 주문 품목 응답 DTO
 */
public record OrderItemResponse(
        Long nbProduct,
        String nmProduct,
        String imagePath,
        Integer unitPrice,
        Integer quantity,
        Integer amount
) {
}