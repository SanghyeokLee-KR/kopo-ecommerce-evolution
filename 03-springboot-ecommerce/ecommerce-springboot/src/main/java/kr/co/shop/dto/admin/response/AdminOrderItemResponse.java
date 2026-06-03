package kr.co.shop.dto.admin.response;

/**
 * 관리자 주문 품목 응답 DTO
 */
public record AdminOrderItemResponse(
        Long nbProduct,
        String nmProduct,
        String imagePath,
        Integer unitPrice,
        Integer quantity,
        Integer amount
) {
}