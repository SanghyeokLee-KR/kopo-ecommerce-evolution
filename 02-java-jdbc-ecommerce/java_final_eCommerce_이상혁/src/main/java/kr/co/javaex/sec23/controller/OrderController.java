package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.OrderService;
import kr.co.javaex.sec23.service.UserService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class OrderController {

    private final OrderService orderService;

    public OrderController(UserService userService) {
        this.orderService = new OrderService(userService);
    }

    public void menu() {
        boolean run = true;

        while (run) {

            System.out.println();
            System.out.println("======================================");
            System.out.println("             주문 / 결제 메뉴");
            System.out.println("======================================");
            System.out.println("1. 장바구니 전체 상품 주문");
            System.out.println("2. 장바구니 선택 상품 주문");
            System.out.println("3. 상품 바로 구매");
            System.out.println("4. 내 주문 내역 조회");
            System.out.println("5. 주문 상세 정보 조회");
            System.out.println("6. 주문 취소");
            System.out.println("0. 이전 메뉴");
            System.out.println("======================================");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    orderService.orderAllCartItems(); // 1. 장바구니 전체 상품 주문
                    break;
                case 2:
                    orderService.orderOneCartItem(); // 2. 장바구니 선택 상품 주문
                    break;
                case 3:
                    orderService.orderProductDirect(); // 3. 상품 바로 구매
                    break;
                case 4:
                    orderService.showMyOrders(); // 4. 내 주문 내역 조회
                    break;
                case 5:
                    orderService.showOrderDetail(); // 5. 주문 상세 정보 조회
                    break;
                case 6:
                    orderService.cancelOrder(); // 6. 주문 취소
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