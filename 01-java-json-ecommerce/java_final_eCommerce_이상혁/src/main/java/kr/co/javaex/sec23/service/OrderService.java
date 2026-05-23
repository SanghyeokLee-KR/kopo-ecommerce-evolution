package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Cart;
import kr.co.javaex.sec23.domain.Order;
import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.domain.User;
import kr.co.javaex.sec23.repository.CartRepository;
import kr.co.javaex.sec23.repository.OrderRepository;
import kr.co.javaex.sec23.repository.ProductRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.ProductStatus.ProductStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>주문 서비스</h3>
 * 항목 - 주문, 바로 주문, 주문 조회, 주문 취소
 */
public class OrderService {

    private final OrderRepository orderRepository = new OrderRepository();
    private final CartRepository cartRepository = new CartRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final UserService userService;

    public OrderService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 장바구니 전체 상품을 주문 처리
     */
    public void orderAllCartItems() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        List<Cart> carts = cartRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<Cart> remainingCarts = new ArrayList<>();

        boolean hasCartItems = false;
        boolean orderCreated = false;

        for (Cart cart : carts) {
            if (!cart.getUserId().equals(currentUser.getUserId())) {
                remainingCarts.add(cart);
                continue;
            }

            hasCartItems = true;
            Product product = findProductById(products, cart.getProductId());

            // 주문 가능 여부 검증
            if (!canOrderProduct(product, cart.getQuantity())) {
                remainingCarts.add(cart);
                continue;
            }

            // 주문 생성 및 재고 차감
            createOrder(orders, currentUser.getUserId(), product, cart.getQuantity());
            deductStock(product, cart.getQuantity());
            orderCreated = true;
        }

        if (!hasCartItems) {
            System.out.println("장바구니가 비어 있습니다.");
            return;
        }

        if (!orderCreated) {
            productRepository.updateAll(products);
            System.out.println("주문 가능한 상품이 없습니다.");
            return;
        }

        // 주문, 장바구니, 재고 변경사항 저장
        orderRepository.updateAll(orders);
        cartRepository.updateAll(remainingCarts);
        productRepository.updateAll(products);

        System.out.println("장바구니 전체 주문이 완료되었습니다.");
    }

    /**
     * 장바구니에 담긴 특정 상품 하나만 주문 처리
     */
    public void orderOneCartItem() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        String productId = InputUtil.inputRequiredLine("주문할 상품 ID 입력: ");

        List<Cart> carts = cartRepository.findAll();
        Cart selectedCart = findMyCartItem(carts, currentUser.getUserId(), productId);

        if (selectedCart == null) {
            System.out.println("장바구니에 해당 상품이 없습니다.");
            return;
        }

        List<Product> products = productRepository.findAll();
        Product product = findProductById(products, productId);

        if (!canOrderProduct(product, selectedCart.getQuantity())) {
            productRepository.updateAll(products);
            return;
        }

        List<Order> orders = orderRepository.findAll();

        // 주문 생성 후 장바구니에서 해당 상품 제거
        createOrder(orders, currentUser.getUserId(), product, selectedCart.getQuantity());
        deductStock(product, selectedCart.getQuantity());
        carts.remove(selectedCart);

        orderRepository.updateAll(orders);
        cartRepository.updateAll(carts);
        productRepository.updateAll(products);

        System.out.println("선택한 상품 주문이 완료되었습니다.");
    }

    /**
     * 장바구니를 거치지 않고 상품을 바로 주문
     */
    public void orderProductDirect() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        String productId = InputUtil.inputRequiredLine("주문할 상품 ID 입력: ");
        List<Product> products = productRepository.findAll();
        Product product = findProductById(products, productId);

        if (product == null) {
            System.out.println("상품 ID를 다시 확인해주세요.");
            return;
        }

        int quantity = InputUtil.inputPositiveInt("주문 수량 입력: ");

        if (!canOrderProduct(product, quantity)) {
            productRepository.updateAll(products);
            return;
        }

        List<Order> orders = orderRepository.findAll();

        // 주문 생성 및 재고 반영
        createOrder(orders, currentUser.getUserId(), product, quantity);
        deductStock(product, quantity);

        orderRepository.updateAll(orders);
        productRepository.updateAll(products);

        System.out.println("상품 주문이 완료되었습니다.");
    }

    /**
     * 로그인한 사용자의 주문 목록을 조회
     */
    public void showMyOrders() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        List<Order> orders = orderRepository.findAll();
        boolean found = false;

        System.out.println("\n===== 내 주문 목록 =====");

        for (Order order : orders) {
            if (order.getUserId().equals(currentUser.getUserId())) {
                Product product = productRepository.findById(order.getProductId());

                System.out.println("주문번호 : " + order.getOrderId());
                System.out.println("상품ID : " + order.getProductId());
                System.out.println("상품명 : " + (product != null ? product.getProductName() : "삭제된 상품"));
                System.out.println("수량 : " + order.getQuantity());
                System.out.println("주문금액 : " + order.getOrderPrice());
                System.out.println("------------------------");

                found = true;
            }
        }

        if (!found) {
            System.out.println("주문 내역이 없습니다.");
        }
    }

    /**
     * 주문 상세 정보 조회
     */
    public void showOrderDetail() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        String orderId = InputUtil.inputRequiredLine("조회할 주문번호 입력: ");
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            if (order.getOrderId().equals(orderId) && order.getUserId().equals(currentUser.getUserId())) {
                Product product = productRepository.findById(order.getProductId());

                System.out.println("\n===== 주문 상세 정보 =====");
                System.out.println("주문번호 : " + order.getOrderId());
                System.out.println("상품ID : " + order.getProductId());
                System.out.println("상품명 : " + (product != null ? product.getProductName() : "삭제된 상품"));
                System.out.println("수량 : " + order.getQuantity());
                System.out.println("주문금액 : " + order.getOrderPrice());
                return;
            }
        }

        System.out.println("주문번호를 다시 확인해주세요.");
    }

    /**
     * 주문을 취소(재고 복구)
     */
    public void cancelOrder() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        String orderId = InputUtil.inputRequiredLine("취소할 주문번호 입력: ");
        List<Order> orders = orderRepository.findAll();

        Order targetOrder = findMyOrder(orders, currentUser.getUserId(), orderId);
        if (targetOrder == null) {
            System.out.println("주문번호를 다시 확인해주세요.");
            return;
        }

        List<Product> products = productRepository.findAll();
        Product product = findProductById(products, targetOrder.getProductId());

        // 주문 취소 시 재고 복구
        if (product != null) {
            restoreStock(product, targetOrder.getQuantity());
            productRepository.updateAll(products);
        }

        orders.remove(targetOrder);
        orderRepository.updateAll(orders);

        System.out.println("주문 취소가 완료되었습니다.");
    }

    private User getLoginUserOrPrintError() {
        User currentUser = userService.getLoginUser();

        if (currentUser == null) {
            System.out.println("로그인 후 이용 가능합니다.");
            return null;
        }

        return currentUser;
    }

    private boolean canOrderProduct(Product product, int quantity) {
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
            product.setStockQuantity(0);
            product.setProductStatus(ProductStatus.품절);
            return false;
        }

        if (product.getStockQuantity() < quantity) {
            System.out.println("재고가 부족합니다.");
            return false;
        }

        return true;
    }

    private void createOrder(List<Order> orders, String userId, Product product, int quantity) {
        int orderPrice = product.getPrice() * quantity;
        String orderId = createOrderId(orders.size() + 1);

        Order newOrder = new Order(orderId, userId, product.getProductId(), quantity, orderPrice);

        orders.add(newOrder);
    }

    private void deductStock(Product product, int quantity) {
        int remainingStock = product.getStockQuantity() - quantity;
        product.setStockQuantity(remainingStock);

        if (remainingStock == 0) {
            product.setProductStatus(ProductStatus.품절);
        }
    }

    private void restoreStock(Product product, int quantity) {
        product.setStockQuantity(product.getStockQuantity() + quantity);

        if (product.getStockQuantity() > 0 && product.getProductStatus() == ProductStatus.품절) {
            product.setProductStatus(ProductStatus.정상);
        }
    }

    private Product findProductById(List<Product> products, String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    private Cart findMyCartItem(List<Cart> carts, String userId, String productId) {
        for (Cart cart : carts) {
            if (cart.getUserId().equals(userId) && cart.getProductId().equals(productId)) {
                return cart;
            }
        }
        return null;
    }

    private Order findMyOrder(List<Order> orders, String userId, String orderId) {
        for (Order order : orders) {
            if (order.getUserId().equals(userId) && order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    // 주문번호 부여
    private String createOrderId(int number) {
        if (number < 10) {
            return "O00" + number;
        } else if (number < 100) {
            return "O0" + number;
        } else {
            return "O" + number;
        }
    }
}