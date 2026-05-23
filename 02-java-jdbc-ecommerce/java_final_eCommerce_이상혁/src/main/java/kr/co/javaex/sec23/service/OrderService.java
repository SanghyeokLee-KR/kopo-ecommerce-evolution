package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.*;
import kr.co.javaex.sec23.repository.CartRepository;
import kr.co.javaex.sec23.repository.OrderItemRepository;
import kr.co.javaex.sec23.repository.OrderRepository;
import kr.co.javaex.sec23.repository.ProductRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.ProductStatus.ProductStatus;
import kr.co.javaex.sec23.util.common.enums.order.OrderStatus;
import kr.co.javaex.sec23.util.db.DBUtil;

import java.sql.Connection;
import java.util.List;

public class OrderService {

    private final OrderRepository orderRepository = new OrderRepository();
    private final OrderItemRepository orderItemRepository = new OrderItemRepository();
    private final CartRepository cartRepository = new CartRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final UserService userService;

    public OrderService(UserService userService) {
        this.userService = userService;
    }

    // 장바구니 전체 상품 주문
    public void orderAllCartItems() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            List<Cart> carts = cartRepository.findByUserNo(con, currentUser.getUserNo());

            if (carts.isEmpty()) {
                System.out.println("장바구니가 비어 있습니다.");
                return;
            }

            int totalPrice = 0;

            for (Cart cart : carts) {
                Product product = productRepository.findByProductNo(con, cart.getProductNo());

                if (!isOrderable(product, cart.getQuantity())) {
                    return;
                }

                totalPrice += product.getPrice() * cart.getQuantity();
            }

            // 주문 생성, 재고 차감, 장바구니 정리를 한 트랜잭션으로 처리
            Order order = Order.builder().userNo(currentUser.getUserNo()).orderStatus(OrderStatus.결제완료).totalPrice(totalPrice).build();

            int orderNo = orderRepository.save(con, order);

            for (Cart cart : carts) {
                Product product = productRepository.findByProductNo(con, cart.getProductNo());

                OrderItem orderItem = OrderItem.builder().orderNo(orderNo).productNo(product.getProductNo()).orderPrice(product.getPrice()).quantity(cart.getQuantity()).itemTotalPrice(product.getPrice() * cart.getQuantity()).build();

                orderItemRepository.save(con, orderItem);

                decreaseStock(product, cart.getQuantity());
                productRepository.update(con, product);
            }

            cartRepository.deleteByUserNo(con, currentUser.getUserNo());

            con.commit();
            System.out.println("장바구니 전체 주문이 완료되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("장바구니 전체 주문 실패: " + e.getMessage());
            throw new RuntimeException("장바구니 전체 주문 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 장바구니 선택 상품 주문
    public void orderOneCartItem() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        String productId = InputUtil.inputRequiredLine("주문할 상품 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Product product = productRepository.findByProductId(con, productId);

            if (product == null) {
                System.out.println("상품 ID를 다시 확인해주세요.");
                return;
            }

            Cart cart = cartRepository.findByUserNoAndProductNo(con, currentUser.getUserNo(), product.getProductNo());

            if (cart == null) {
                System.out.println("장바구니에 해당 상품이 없습니다.");
                return;
            }

            if (!isOrderable(product, cart.getQuantity())) {
                return;
            }

            int totalPrice = product.getPrice() * cart.getQuantity();

            Order order = Order.builder().userNo(currentUser.getUserNo()).orderStatus(OrderStatus.결제완료).totalPrice(totalPrice).build();

            int orderNo = orderRepository.save(con, order);

            OrderItem orderItem = OrderItem.builder().orderNo(orderNo).productNo(product.getProductNo()).orderPrice(product.getPrice()).quantity(cart.getQuantity()).itemTotalPrice(totalPrice).build();

            orderItemRepository.save(con, orderItem);

            decreaseStock(product, cart.getQuantity());
            productRepository.update(con, product);

            cartRepository.deleteByCartNo(con, cart.getCartNo());

            con.commit();
            System.out.println("선택한 상품 주문이 완료되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("선택 주문 실패: " + e.getMessage());
            throw new RuntimeException("선택 주문 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    //  상품 바로 구매
    public void orderProductDirect() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        String productId = InputUtil.inputRequiredLine("주문할 상품 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Product product = productRepository.findByProductId(con, productId);

            if (product == null) {
                System.out.println("상품 ID를 다시 확인해주세요.");
                return;
            }

            int quantity = InputUtil.inputPositiveInt("주문 수량 입력: ");

            if (!isOrderable(product, quantity)) {
                return;
            }

            int totalPrice = product.getPrice() * quantity;

            Order order = Order.builder().userNo(currentUser.getUserNo()).orderStatus(OrderStatus.결제완료).totalPrice(totalPrice).build();

            int orderNo = orderRepository.save(con, order);

            OrderItem orderItem = OrderItem.builder().orderNo(orderNo).productNo(product.getProductNo()).orderPrice(product.getPrice()).quantity(quantity).itemTotalPrice(totalPrice).build();

            orderItemRepository.save(con, orderItem);

            decreaseStock(product, quantity);
            productRepository.update(con, product);

            con.commit();
            System.out.println("상품 주문이 완료되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("상품 바로 주문 실패: " + e.getMessage());
            throw new RuntimeException("상품 바로 주문 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 내 주문 내역 조회
    public void showMyOrders() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        try (Connection con = DBUtil.getConnection()) {
            List<Order> orders = orderRepository.findByUserNo(con, currentUser.getUserNo());

            if (orders.isEmpty()) {
                System.out.println("주문 내역이 없습니다.");
                return;
            }

            System.out.println("\n===== 내 주문 목록 =====");

            for (Order order : orders) {
                System.out.println("주문번호 : " + order.getOrderNo());
                System.out.println("주문상태 : " + order.getOrderStatus());
                System.out.println("주문일자 : " + order.getOrderDate());
                System.out.println("총주문금액 : " + order.getTotalPrice());
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            System.out.println("주문 목록 조회 실패: " + e.getMessage());
            throw new RuntimeException("주문 목록 조회 실패", e);
        }
    }

    // 주문 상세 정보 조회
    public void showOrderDetail() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        int orderNo = InputUtil.inputInt("조회할 주문번호 입력: ");

        try (Connection con = DBUtil.getConnection()) {
            Order order = orderRepository.findByOrderNo(con, orderNo);

            if (order == null || order.getUserNo() != currentUser.getUserNo()) {
                System.out.println("주문번호를 다시 확인해주세요.");
                return;
            }

            List<OrderItem> items = orderItemRepository.findByOrderNo(con, orderNo);

            System.out.println("\n===== 주문 상세 정보 =====");
            System.out.println("주문번호 : " + order.getOrderNo());
            System.out.println("주문상태 : " + order.getOrderStatus());
            System.out.println("주문일자 : " + order.getOrderDate());
            System.out.println("총주문금액 : " + order.getTotalPrice());
            System.out.println("------------------------");

            for (OrderItem item : items) {
                Product product = productRepository.findByProductNo(con, item.getProductNo());

                System.out.println("상품번호 : " + item.getProductNo());
                System.out.println("상품명 : " + (product != null ? product.getProductName() : "삭제된 상품"));
                System.out.println("주문가격 : " + item.getOrderPrice());
                System.out.println("수량 : " + item.getQuantity());
                System.out.println("소계 : " + item.getItemTotalPrice());
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            System.out.println("주문 상세 조회 실패: " + e.getMessage());
            throw new RuntimeException("주문 상세 조회 실패", e);
        }
    }

    // 주문 취소
    public void cancelOrder() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        int orderNo = InputUtil.inputInt("취소할 주문번호 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Order order = orderRepository.findByOrderNo(con, orderNo);

            if (order == null || order.getUserNo() != currentUser.getUserNo()) {
                System.out.println("주문번호를 다시 확인해주세요.");
                return;
            }

            if (order.getOrderStatus() == OrderStatus.주문취소) {
                System.out.println("이미 취소된 주문입니다.");
                return;
            }

            List<OrderItem> items = orderItemRepository.findByOrderNo(con, orderNo);

            // 주문 취소 시 주문 수량만큼 재고를 되돌린다.
            for (OrderItem item : items) {
                Product product = productRepository.findByProductNo(con, item.getProductNo());

                if (product != null) {
                    increaseStock(product, item.getQuantity());
                    productRepository.update(con, product);
                }
            }

            orderRepository.updateStatus(con, orderNo, OrderStatus.주문취소);

            con.commit();
            System.out.println("주문 취소가 완료되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("주문 취소 실패: " + e.getMessage());
            throw new RuntimeException("주문 취소 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    private User getLoginUser() {
        User currentUser = userService.getLoginUser();

        if (currentUser == null) {
            System.out.println("로그인 후 이용 가능합니다.");
            return null;
        }

        return currentUser;
    }

    private boolean isOrderable(Product product, int quantity) {
        if (product == null) {
            System.out.println("상품 ID를 다시 확인해주세요.");
            return false;
        }

        if (product.getProductStatus() == ProductStatus.판매중지) {
            System.out.println("판매중지된 상품입니다.");
            return false;
        }

        if (product.getProductStatus() == ProductStatus.품절 || product.getStockQuantity() == 0) {
            System.out.println("품절된 상품입니다.");
            return false;
        }

        if (product.getStockQuantity() < quantity) {
            System.out.println("재고가 부족합니다.");
            return false;
        }

        return true;
    }

    private void decreaseStock(Product product, int quantity) {
        int remainingStock = product.getStockQuantity() - quantity;
        product.setStockQuantity(remainingStock);

        if (remainingStock == 0) {
            product.setProductStatus(ProductStatus.품절);
        }
    }

    private void increaseStock(Product product, int quantity) {
        product.setStockQuantity(product.getStockQuantity() + quantity);

        if (product.getStockQuantity() > 0 && product.getProductStatus() == ProductStatus.품절) {
            product.setProductStatus(ProductStatus.정상);
        }
    }
}
