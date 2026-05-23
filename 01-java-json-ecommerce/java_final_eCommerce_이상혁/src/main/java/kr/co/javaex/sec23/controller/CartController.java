package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.CartService;
import kr.co.javaex.sec23.service.UserService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class CartController {

    private final CartService cartService;

    public CartController(UserService userService) {
        this.cartService = new CartService(userService);
    }
    /**
     * <h3>장바구니 메뉴</h3>
     */
    public void menu() {
        boolean run = true;

        while (run) {
            System.out.println("\n===== 장바구니 메뉴 =====");
            System.out.println("1. 장바구니 담기");
            System.out.println("2. 장바구니 조회");
            System.out.println("3. 수량 수정");
            System.out.println("4. 선택 상품 삭제");
            System.out.println("5. 장바구니 비우기");
            System.out.println("0. 이전 메뉴");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    cartService.addCart(); // 장바구니 담기
                    break;
                case 2:
                    cartService.showMyCart(); // 장바구니 조회
                    break;
                case 3:
                    cartService.updateCartQuantity(); // 장바구니 수량 수정
                    break;
                case 4:
                    cartService.deleteCartItem(); // 선택 상품 삭제
                    break;
                case 5:
                    cartService.clearMyCart(); // 장바구니 비우기
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }
}