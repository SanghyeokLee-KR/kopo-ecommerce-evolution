package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Cart;
import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.domain.User;
import kr.co.javaex.sec23.repository.CartRepository;
import kr.co.javaex.sec23.repository.ProductRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.ProductStatus.ProductStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>장바구니 서비스</h3>
 * 항목 - 담기, 조회, 수량 수정, 삭제, 비우기
 */
public class CartService {

    private final CartRepository cartRepository = new CartRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final UserService userService;

    public CartService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 상품을 장바구니에 담는다.
     * 이미 담긴 상품이면 수량만 증가
     */
    public void addCart() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        System.out.println("\n===== 장바구니 담기 =====");

        String productId = InputUtil.inputLine("상품 ID 입력: ");
        Product product = productRepository.findById(productId);

        // 장바구니에 담을 수 있는 상품인지 확인
        if (!canUseProductForCart(product)) {
            return;
        }

        int quantity = InputUtil.inputPositiveInt("수량 입력: ");

        if (quantity > product.getStockQuantity()) {
            System.out.println("재고 수량보다 많이 담을 수 없습니다.");
            return;
        }

        List<Cart> carts = cartRepository.findAll();

        for (Cart cart : carts) {
            if (isMyCartItem(cart, currentUser.getUserId(), productId)) {
                int updatedQuantity = cart.getQuantity() + quantity;

                if (updatedQuantity > product.getStockQuantity()) {
                    System.out.println("재고 수량보다 많이 담을 수 없습니다.");
                    return;
                }

                cart.setQuantity(updatedQuantity);
                cartRepository.updateAll(carts);
                System.out.println("장바구니 수량이 변경되었습니다.");
                return;
            }
        }

        Cart newCart = new Cart(currentUser.getUserId(), productId, quantity);
        cartRepository.save(newCart);

        System.out.println("장바구니에 담았습니다.");
    }

    /**
     * 현재 로그인한 사용자의 장바구니 목록과 총 금액을 조회
     */
    public void showMyCart() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        List<Cart> carts = cartRepository.findAll();
        boolean found = false;
        int totalPrice = 0;

        System.out.println("\n===== 내 장바구니 =====");

        for (Cart cart : carts) {
            if (cart.getUserId().equals(currentUser.getUserId())) {
                Product product = productRepository.findById(cart.getProductId());

                if (product != null) {
                    int itemTotal = product.getPrice() * cart.getQuantity();

                    System.out.println("상품ID : " + product.getProductId());
                    System.out.println("상품명 : " + product.getProductName());
                    System.out.println("가격 : " + product.getPrice());
                    System.out.println("수량 : " + cart.getQuantity());
                    System.out.println("합계 : " + itemTotal);
                    System.out.println("------------------------");

                    totalPrice += itemTotal;
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("장바구니가 비어 있습니다.");
            return;
        }

        System.out.println("총 금액 : " + totalPrice);
    }

    /**
     * 장바구니에 담긴 상품의 수량을 수정
     */
    public void updateCartQuantity() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        System.out.println("\n===== 장바구니 수량 수정 =====");

        String productId = InputUtil.inputLine("상품 ID 입력: ");
        List<Cart> carts = cartRepository.findAll();

        for (Cart cart : carts) {
            if (isMyCartItem(cart, currentUser.getUserId(), productId)) {
                Product product = productRepository.findById(productId);

                if (product == null) {
                    System.out.println("상품 ID를 다시 확인해주세요.");
                    return;
                }

                int quantity = InputUtil.inputPositiveInt("새 수량 입력: ");

                if (quantity > product.getStockQuantity()) {
                    System.out.println("재고 수량보다 많이 담을 수 없습니다.");
                    return;
                }

                cart.setQuantity(quantity);
                cartRepository.updateAll(carts);

                System.out.println("수량이 수정되었습니다.");
                return;
            }
        }

        System.out.println("장바구니에 해당 상품이 없습니다.");
    }

    /**
     * 특정 상품 하나를 삭제
     */
    public void deleteCartItem() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        System.out.println("\n===== 장바구니 상품 삭제 =====");

        String productId = InputUtil.inputLine("삭제할 상품 ID 입력: ");
        List<Cart> carts = cartRepository.findAll();

        Cart targetCart = null;

        for (Cart cart : carts) {
            if (isMyCartItem(cart, currentUser.getUserId(), productId)) {
                targetCart = cart;
                break;
            }
        }

        if (targetCart == null) {
            System.out.println("장바구니에 해당 상품이 없습니다.");
            return;
        }

        carts.remove(targetCart);
        cartRepository.updateAll(carts);

        System.out.println("장바구니 상품이 삭제되었습니다.");
    }

    /**
     * 장바구니를 전체 삭제
     */
    public void clearMyCart() {
        User currentUser = getLoginUserOrPrintError();
        if (currentUser == null) {
            return;
        }

        List<Cart> carts = cartRepository.findAll();
        List<Cart> remainingCarts = new ArrayList<>();

        for (Cart cart : carts) {
            if (!cart.getUserId().equals(currentUser.getUserId())) {
                remainingCarts.add(cart);
            }
        }

        cartRepository.updateAll(remainingCarts);
        System.out.println("장바구니를 비웠습니다.");
    }

    public List<Cart> getMyCartList() {
        User currentUser = userService.getLoginUser();
        List<Cart> myCarts = new ArrayList<>();

        if (currentUser == null) {
            return myCarts;
        }

        List<Cart> carts = cartRepository.findAll();

        for (Cart cart : carts) {
            if (cart.getUserId().equals(currentUser.getUserId())) {
                myCarts.add(cart);
            }
        }

        return myCarts;
    }

    private User getLoginUserOrPrintError() {
        User currentUser = userService.getLoginUser();

        if (currentUser == null) {
            System.out.println("로그인 후 이용 가능합니다.");
            return null;
        }

        return currentUser;
    }

    private boolean canUseProductForCart(Product product) {
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

        return true;
    }

    private boolean isMyCartItem(Cart cart, String userId, String productId) {
        return cart.getUserId().equals(userId) && cart.getProductId().equals(productId);
    }
}