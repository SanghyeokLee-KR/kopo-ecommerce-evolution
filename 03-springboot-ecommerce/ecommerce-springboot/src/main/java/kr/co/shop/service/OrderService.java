package kr.co.shop.service;

import kr.co.shop.dto.order.response.OrderCompleteResponse;
import kr.co.shop.dto.order.response.OrderDetailResponse;
import kr.co.shop.dto.order.response.OrderListResponse;

import java.util.List;

/**
 * 주문 서비스
 */
public interface OrderService {

    /**
     * 장바구니 상품 주문
     */
    OrderCompleteResponse orderFromBasket(Long nbUser, List<Long> nbBasketItemIds);

    /**
     * 상품 바로 주문
     */
    OrderCompleteResponse orderDirect(Long nbUser, Long nbProduct, int quantity);

    /**
     * 내 주문 목록 조회
     */
    List<OrderListResponse> findMyOrders(Long nbUser);

    /**
     * 내 주문 상세 조회
     */
    OrderDetailResponse findMyOrderDetail(Long nbUser, Long nbOrder);
}