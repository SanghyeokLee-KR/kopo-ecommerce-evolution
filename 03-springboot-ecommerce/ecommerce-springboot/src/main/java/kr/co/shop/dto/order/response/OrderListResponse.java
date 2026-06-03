package kr.co.shop.dto.order.response;

/**
 * 주문 목록 응답 DTO
 */
public record OrderListResponse(
        Long nbOrder,
        String daOrder,
        String stOrder,
        Integer orderAmount
) {
}