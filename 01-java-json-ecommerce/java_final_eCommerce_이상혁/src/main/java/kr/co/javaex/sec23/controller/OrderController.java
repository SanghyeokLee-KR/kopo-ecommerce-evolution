package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.OrderService;
import kr.co.javaex.sec23.service.UserService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class OrderController {

    private final OrderService orderService;

    public OrderController(UserService userService) {
        this.orderService = new OrderService(userService);
    }

    /**
     * <h3>주문 메뉴</h3>
     */
    public void menu() {
        boolean run = true;

        while (run) {
            System.out.println("\n===== 주문 메뉴 =====");
            System.out.println("1. 장바구니 전체 주문");
            System.out.println("2. 장바구니 선택 주문");
            System.out.println("3. 상품 바로 주문");
            System.out.println("4. 내 주문 목록 조회");
            System.out.println("5. 주문 상세 조회");
            System.out.println("6. 주문 취소");
            System.out.println("0. 이전 메뉴");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    orderService.orderAllCartItems(); // 장바구니 전체 주문
                    break;
                case 2:
                    orderService.orderOneCartItem(); // 장바구니 선택 주문
                    break;
                case 3:
                    orderService.orderProductDirect(); // 상품 바로 주문
                    break;
                case 4:
                    orderService.showMyOrders(); // 내 주문 목록 조회
                    break;
                case 5:
                    orderService.showOrderDetail(); // 주문 상세 조회
                    break;
                case 6:
                    orderService.cancelOrder(); // 주문 취소
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