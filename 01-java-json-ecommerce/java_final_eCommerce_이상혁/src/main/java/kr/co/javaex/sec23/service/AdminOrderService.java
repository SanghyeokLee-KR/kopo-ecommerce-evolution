package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Order;
import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.domain.User;
import kr.co.javaex.sec23.repository.OrderRepository;
import kr.co.javaex.sec23.repository.ProductRepository;
import kr.co.javaex.sec23.repository.UserRepository;
import kr.co.javaex.sec23.util.common.InputUtil;

import java.util.List;

/**
 * <h3> 관리자용 주문 관리 서비스 </h3>
 * - 전체 주문 목록 조회
 * - 주문 상세 조회
 */
public class AdminOrderService {

    private final OrderRepository orderRepository = new OrderRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final UserRepository userRepository = new UserRepository();

    /**
     * 전체 주문 목록 조회
     */
    public void showAllOrders() {
        List<Order> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            System.out.println("주문 내역이 없습니다.");
            return;
        }

        System.out.println("\n===== 전체 주문 목록 =====");

        for (Order order : orders) {
            // 주문과 연결된 회원, 상품 정보를 함께 조회
            User orderUser = userRepository.findByUserId(order.getUserId());
            Product orderProduct = productRepository.findById(order.getProductId());

            System.out.println("주문번호 : " + order.getOrderId());
            System.out.println("회원ID : " + order.getUserId());
            System.out.println("회원명 : " + (orderUser != null ? orderUser.getUserName() : "알 수 없음"));
            System.out.println("상품ID : " + order.getProductId());
            System.out.println("상품명 : " + (orderProduct != null ? orderProduct.getProductName() : "삭제한 상품"));
            System.out.println("수량 : " + order.getQuantity());
            System.out.println("주문금액 : " + order.getOrderPrice());
            System.out.println("------------------------");
        }
    }

    /**
     * 상세 정보 조회
     */
    public void showOrderDetail() {
        String orderId = InputUtil.inputLine("조회할 주문번호 입력: ");
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                // 주문 상세 조회 시에도 회원, 상품 정보를 함께 출력
                User orderUser = userRepository.findByUserId(order.getUserId());
                Product orderProduct = productRepository.findById(order.getProductId());

                System.out.println("\n===== 주문 상세 정보 =====");
                System.out.println("주문번호 : " + order.getOrderId());
                System.out.println("회원ID : " + order.getUserId());
                System.out.println("회원명 : " + (orderUser != null ? orderUser.getUserName() : "알 수 없음"));
                System.out.println("상품ID : " + order.getProductId());
                System.out.println("상품명 : " + (orderProduct != null ? orderProduct.getProductName() : "삭제한 상품"));
                System.out.println("수량 : " + order.getQuantity());
                System.out.println("주문금액 : " + order.getOrderPrice());
                return;
            }
        }

        System.out.println("해당 주문이 없습니다.");
    }
}