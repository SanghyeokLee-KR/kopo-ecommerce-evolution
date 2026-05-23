package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.CartService;
import kr.co.javaex.sec23.service.UserService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class CartController {

    private final CartService cartService;

    public CartController(UserService userService) {
        this.cartService = new CartService(userService);
    }

    public void menu() {
        boolean run = true;

        while (run) {

            System.out.println();
            System.out.println("======================================");
            System.out.println("           장바구니 관리 메뉴");
            System.out.println("======================================");
            System.out.println("1. 상품 장바구니 담기");
            System.out.println("2. 내 장바구니 조회");
            System.out.println("3. 상품 수량 변경");
            System.out.println("4. 선택 상품 삭제");
            System.out.println("5. 장바구니 전체 비우기");
            System.out.println("0. 이전 메뉴");
            System.out.println("======================================");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    cartService.addCart(); // 1. 상품 장바구니 담기
                    break;
                case 2:
                    cartService.showMyCart(); // 2. 내 장바구니 조회
                    break;
                case 3:
                    cartService.updateCartQuantity(); // 3. 상품 수량 변경
                    break;
                case 4:
                    cartService.deleteCartItem(); // 4. 선택 상품 삭제
                    break;
                case 5:
                    cartService.clearMyCart(); // 5. 장바구니 전체 비우기
                    break;
                case 0:
                    run = false; // 0. 이전 메뉴
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }
}