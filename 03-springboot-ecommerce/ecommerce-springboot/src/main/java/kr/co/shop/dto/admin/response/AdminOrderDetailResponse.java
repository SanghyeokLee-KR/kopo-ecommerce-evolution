package kr.co.shop.dto.admin.response;

import java.util.List;

/**
 * 관리자 주문 상세 응답 DTO
 */
public record AdminOrderDetailResponse(
        Long nbOrder,
        Long nbUser,
        String nmUser,
        String nmOrderPerson,
        String nmDeliveryAddress,
        String daOrder,
        String stOrder,
        Integer orderAmount,

        // 관리자 주문 품목 목록
        List<AdminOrderItemResponse> items
) {

}