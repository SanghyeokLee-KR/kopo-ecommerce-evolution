package kr.co.shop.dto.order.response;

import java.util.List;

/**
 * 주문 상세 응답 DTO
 */
public record OrderDetailResponse(
        Long nbOrder,
        String nmOrderPerson,
        String nmDeliveryAddress,
        String daOrder,
        String stOrder,
        Integer orderAmount,

        // 주문 품목 목록
        List<OrderItemResponse> items
) {
}