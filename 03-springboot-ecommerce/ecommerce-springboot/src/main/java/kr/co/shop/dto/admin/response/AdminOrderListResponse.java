package kr.co.shop.dto.admin.response;

/**
 * 관리자 주문 목록 응답 DTO
 */
public record AdminOrderListResponse(
        Long nbOrder,
        Long nbUser,
        String nmUser,
        String nmOrderPerson,
        String daOrder,
        String stOrder,
        Integer orderAmount
) {
}