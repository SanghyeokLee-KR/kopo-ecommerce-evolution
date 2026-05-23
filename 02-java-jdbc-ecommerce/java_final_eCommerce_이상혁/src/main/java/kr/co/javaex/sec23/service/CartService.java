package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Cart;
import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.domain.User;
import kr.co.javaex.sec23.repository.CartRepository;
import kr.co.javaex.sec23.repository.ProductRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.ProductStatus.ProductStatus;
import kr.co.javaex.sec23.util.db.DBUtil;

import java.sql.Connection;
import java.util.List;

public class CartService {

    private final CartRepository cartRepository = new CartRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final UserService userService;

    public CartService(UserService userService) {
        this.userService = userService;
    }

    // 장바구니 담기
    public void addCart() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        System.out.println("\n===== 장바구니 담기 =====");

        String productId = InputUtil.inputRequiredLine("상품 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Product product = productRepository.findByProductId(con, productId);

            if (!isAvailable(product)) {
                return;
            }

            int quantity = InputUtil.inputPositiveInt("수량 입력: ");

            if (quantity > product.getStockQuantity()) {
                System.out.println("재고 수량보다 많이 담을 수 없습니다.");
                return;
            }

            Cart existingCart = cartRepository.findByUserNoAndProductNo(
                    con,
                    currentUser.getUserNo(),
                    product.getProductNo()
            );

            if (existingCart != null) {
                // 같은 상품이 이미 담겨 있으면 수량만 갱신
                int updatedQuantity = existingCart.getQuantity() + quantity;

                if (updatedQuantity > product.getStockQuantity()) {
                    System.out.println("재고 수량보다 많이 담을 수 없습니다.");
                    return;
                }

                cartRepository.updateQuantity(con, existingCart.getCartNo(), updatedQuantity);
                con.commit();
                System.out.println("장바구니 수량이 변경되었습니다.");
                return;
            }

            Cart newCart = Cart.builder()
                    .userNo(currentUser.getUserNo())
                    .productNo(product.getProductNo())
                    .quantity(quantity)
                    .build();

            cartRepository.save(con, newCart);

            con.commit();
            System.out.println("장바구니에 담았습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("장바구니 담기 실패: " + e.getMessage());
            throw new RuntimeException("장바구니 담기 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 내 장바구니 조회
    public void showMyCart() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        try (Connection con = DBUtil.getConnection()) {

            List<Cart> carts =
                    cartRepository.findWithProductByUserNo(con, currentUser.getUserNo());

            if (carts.isEmpty()) {
                System.out.println("장바구니가 비어 있습니다.");
                return;
            }

            int totalPrice = 0;

            System.out.println("\n===== 내 장바구니 =====");

            for (Cart cart : carts) {
                int itemTotal = cart.getPrice() * cart.getQuantity();

                System.out.println("장바구니번호 : " + cart.getCartNo());
                System.out.println("상품번호 : " + cart.getProductNo());
                System.out.println("상품ID : " + cart.getProductId());
                System.out.println("상품명 : " + cart.getProductName());
                System.out.println("가격 : " + cart.getPrice());
                System.out.println("수량 : " + cart.getQuantity());
                System.out.println("합계 : " + itemTotal);
                System.out.println("------------------------");

                totalPrice += itemTotal;
            }

            System.out.println("총 금액 : " + totalPrice);

        } catch (Exception e) {
            System.out.println("장바구니 조회 실패: " + e.getMessage());
            throw new RuntimeException("장바구니 조회 실패", e);
        }
    }

    // 장바구니 수량 수정
    public void updateCartQuantity() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        System.out.println("\n===== 장바구니 수량 수정 =====");
        String productId = InputUtil.inputRequiredLine("상품 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Product product = productRepository.findByProductId(con, productId);

            if (product == null) {
                System.out.println("상품 ID를 다시 확인해주세요.");
                return;
            }

            Cart cart = cartRepository.findByUserNoAndProductNo(
                    con,
                    currentUser.getUserNo(),
                    product.getProductNo()
            );

            if (cart == null) {
                System.out.println("장바구니에 해당 상품이 없습니다.");
                return;
            }

            int quantity = InputUtil.inputPositiveInt("새 수량 입력: ");

            if (quantity > product.getStockQuantity()) {
                System.out.println("재고 수량보다 많이 담을 수 없습니다.");
                return;
            }

            cartRepository.updateQuantity(con, cart.getCartNo(), quantity);

            con.commit();
            System.out.println("수량이 수정되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("장바구니 수량 수정 실패: " + e.getMessage());
            throw new RuntimeException("장바구니 수량 수정 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 장바구니 상품 삭제
    public void deleteCartItem() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        System.out.println("\n===== 장바구니 상품 삭제 =====");
        String productId = InputUtil.inputRequiredLine("삭제할 상품 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Product product = productRepository.findByProductId(con, productId);

            if (product == null) {
                System.out.println("상품 ID를 다시 확인해주세요.");
                return;
            }

            Cart cart = cartRepository.findByUserNoAndProductNo(
                    con,
                    currentUser.getUserNo(),
                    product.getProductNo()
            );

            if (cart == null) {
                System.out.println("장바구니에 해당 상품이 없습니다.");
                return;
            }

            cartRepository.deleteByCartNo(con, cart.getCartNo());

            con.commit();
            System.out.println("장바구니 상품이 삭제되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("장바구니 상품 삭제 실패: " + e.getMessage());
            throw new RuntimeException("장바구니 상품 삭제 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 장바구니 전체 비우기
    public void clearMyCart() {
        User currentUser = getLoginUser();
        if (currentUser == null) {
            return;
        }

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            cartRepository.deleteByUserNo(con, currentUser.getUserNo());

            con.commit();
            System.out.println("장바구니를 비웠습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("장바구니 비우기 실패: " + e.getMessage());
            throw new RuntimeException("장바구니 비우기 실패", e);
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

    private boolean isAvailable(Product product) {
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
}
