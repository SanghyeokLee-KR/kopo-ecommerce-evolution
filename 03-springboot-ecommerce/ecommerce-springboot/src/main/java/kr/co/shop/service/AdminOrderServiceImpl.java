package kr.co.shop.service;

import kr.co.shop.common.enums.OrderStatus;
import kr.co.shop.common.exception.BusinessException;
import kr.co.shop.common.exception.ErrorCode;
import kr.co.shop.domain.Order;
import kr.co.shop.dto.admin.response.AdminOrderDetailResponse;
import kr.co.shop.dto.admin.response.AdminOrderItemResponse;
import kr.co.shop.dto.admin.response.AdminOrderListResponse;
import kr.co.shop.repository.jpa.OrderRepository;
import kr.co.shop.repository.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Page<AdminOrderListResponse> findAllOrders(
            int page,
            String startDate,
            String endDate,
            String keyword
    ) {
        int pageNum = Math.max(page - 1, 0);
        int size = 6;
        int offset = pageNum * size;

        List<AdminOrderListResponse> orders =
                orderMapper.findAllOrders(startDate, endDate, keyword, offset, size);

        int total =
                orderMapper.countAllOrders(startDate, endDate, keyword);

        return new PageImpl<>(orders, PageRequest.of(pageNum, size), total);
    }

    @Override
    public AdminOrderDetailResponse findOrderDetail(Long nbOrder) {
        AdminOrderDetailResponse order = orderMapper.findAdminOrderDetail(nbOrder);

        if (order == null) {
            log.warn("관리자 주문 상세 조회 실패 - 존재하지 않는 주문: nbOrder={}", nbOrder);
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        List<AdminOrderItemResponse> items = orderMapper.findAdminOrderItems(nbOrder);

        return new AdminOrderDetailResponse(
                order.nbOrder(),
                order.nbUser(),
                order.nmUser(),
                order.nmOrderPerson(),
                order.nmDeliveryAddress(),
                order.daOrder(),
                order.stOrder(),
                order.orderAmount(),
                items
        );
    }

    @Override
    @Transactional
    public void changeOrderStatus(Long nbOrder, String stOrder) {
        if (!OrderStatus.isValid(stOrder)) {
            log.warn("유효하지 않은 주문 상태 코드 - nbOrder={}, stOrder={}", nbOrder, stOrder);
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        }

        Order order = orderRepository.findById(nbOrder)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        String beforeStatus = order.getStOrder();
        order.changeStatus(stOrder);

        log.info("주문 상태 변경 완료 - nbOrder={}, {} → {}", nbOrder, beforeStatus, stOrder);
    }
}