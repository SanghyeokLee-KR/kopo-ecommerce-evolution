package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Order;
import kr.co.javaex.sec23.domain.OrderItem;
import kr.co.javaex.sec23.repository.OrderItemRepository;
import kr.co.javaex.sec23.repository.OrderRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.db.DBUtil;

import java.sql.Connection;
import java.util.List;

public class AdminOrderService {

    private final OrderRepository orderRepository = new OrderRepository();
    private final OrderItemRepository orderItemRepository = new OrderItemRepository();

    // 전체 주문 조회
    public void showAllOrders() {
        try (Connection con = DBUtil.getConnection()) {
            List<Order> orders = orderRepository.findAllWithUser(con);

            if (orders.isEmpty()) {
                System.out.println("주문 내역이 없습니다.");
                return;
            }

            System.out.println("\n===== 전체 주문 목록 =====");

            for (Order order : orders) {
                System.out.println("주문번호 : " + order.getOrderNo());
                System.out.println("회원번호 : " + order.getUserNo());
                System.out.println("회원명 : " + order.getUserName());
                System.out.println("주문상태 : " + order.getOrderStatus());
                System.out.println("주문일자 : " + order.getOrderDate());
                System.out.println("총주문금액 : " + order.getTotalPrice());
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            System.out.println("전체 주문 조회 실패: " + e.getMessage());
            throw new RuntimeException("전체 주문 조회 실패", e);
        }
    }

    // 주문 상세 조회
    public void showOrderDetail() {
        int orderNo = InputUtil.inputInt("조회할 주문번호 입력: ");

        try (Connection con = DBUtil.getConnection()) {
            Order order = orderRepository.findDetailWithUserByOrderNo(con, orderNo);

            if (order == null) {
                System.out.println("해당 주문이 없습니다.");
                return;
            }

            List<OrderItem> items = orderItemRepository.findWithProductByOrderNo(con, orderNo);

            System.out.println("\n===== 주문 상세 정보 =====");
            System.out.println("주문번호 : " + order.getOrderNo());
            System.out.println("회원번호 : " + order.getUserNo());
            System.out.println("회원명 : " + order.getUserName());
            System.out.println("주문상태 : " + order.getOrderStatus());
            System.out.println("주문일자 : " + order.getOrderDate());
            System.out.println("총주문금액 : " + order.getTotalPrice());
            System.out.println("------------------------");

            for (OrderItem item : items) {
                System.out.println("주문상세번호 : " + item.getOrderItemNo());
                System.out.println("상품번호 : " + item.getProductNo());
                System.out.println("상품명 : " + item.getProductName());
                System.out.println("주문가격 : " + item.getOrderPrice());
                System.out.println("수량 : " + item.getQuantity());
                System.out.println("소계금액 : " + item.getItemTotalPrice());
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            System.out.println("주문 상세 조회 실패: " + e.getMessage());
            throw new RuntimeException("주문 상세 조회 실패", e);
        }
    }
}