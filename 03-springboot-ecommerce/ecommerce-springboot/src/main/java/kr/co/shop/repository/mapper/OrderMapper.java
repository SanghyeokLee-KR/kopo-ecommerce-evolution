package kr.co.shop.repository.mapper;

import kr.co.shop.dto.admin.response.AdminOrderDetailResponse;
import kr.co.shop.dto.admin.response.AdminOrderItemResponse;
import kr.co.shop.dto.admin.response.AdminOrderListResponse;
import kr.co.shop.dto.order.response.OrderDetailResponse;
import kr.co.shop.dto.order.response.OrderItemResponse;
import kr.co.shop.dto.order.response.OrderListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 주문 조회 Mapper
 */
@Mapper
public interface OrderMapper {

    // 사용자 주문 목록 조회
    List<OrderListResponse> findMyOrders(
            @Param("nbUser") Long nbUser
    );

    // 사용자 주문 상세 기본 정보 조회
    OrderDetailResponse findMyOrderDetail(
            @Param("nbUser") Long nbUser,
            @Param("nbOrder") Long nbOrder
    );

    // 사용자 주문 상세 품목 조회
    List<OrderItemResponse> findMyOrderItems(
            @Param("nbOrder") Long nbOrder
    );

    // 관리자 주문 목록 페이징 조회
    List<AdminOrderListResponse> findAllOrders(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // 관리자 주문 목록 총 개수
    int countAllOrders(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("keyword") String keyword
    );

    // 관리자 주문 상세 기본 정보 조회
    AdminOrderDetailResponse findAdminOrderDetail(
            @Param("nbOrder") Long nbOrder
    );

    // 관리자 주문 상세 품목 조회
    List<AdminOrderItemResponse> findAdminOrderItems(
            @Param("nbOrder") Long nbOrder
    );
}