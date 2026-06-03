package kr.co.shop.service;

import kr.co.shop.dto.admin.response.AdminOrderDetailResponse;
import kr.co.shop.dto.admin.response.AdminOrderListResponse;
import org.springframework.data.domain.Page;

/**
 * 관리자 주문 관리 서비스
 */
public interface AdminOrderService {

    /**
     * 전체 주문 목록 조회
     */
    Page<AdminOrderListResponse> findAllOrders(int page, String startDate, String endDate, String keyword);

    /**
     * 주문 상세 조회
     */
    AdminOrderDetailResponse findOrderDetail(Long nbOrder);

    /**
     * 주문 상태 변경
     */
    void changeOrderStatus(Long nbOrder, String stOrder);

}