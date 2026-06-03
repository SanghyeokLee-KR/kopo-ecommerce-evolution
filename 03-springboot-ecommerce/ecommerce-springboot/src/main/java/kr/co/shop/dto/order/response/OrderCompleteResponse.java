package kr.co.shop.dto.order.response;

import java.util.List;

/**
 * 주문 완료 응답 DTO
 */
public record OrderCompleteResponse(
        Long nbOrder,
        String nmOrderPerson,
        String daOrder,
        String stOrder,
        Integer orderAmount,

        // 주문 품목 목록
        List<OrderItemResponse> items
) {
}