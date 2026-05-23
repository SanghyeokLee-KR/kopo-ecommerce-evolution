package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.AdminOrderService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class AdminOrderController {

    private final AdminOrderService adminOrderService = new AdminOrderService();

    public void menu() {
        boolean run = true;

        while (run) {
            System.out.println("\n===== 관리자 주문 관리 =====");
            System.out.println("1. 전체 주문 조회");
            System.out.println("2. 주문 상세 조회");
            System.out.println("0. 이전 메뉴");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    adminOrderService.showAllOrders(); // 1. 전체 주문 조회
                    break;
                case 2:
                    adminOrderService.showOrderDetail(); // 2. 주문 상세 조회
                    break;
                case 0:
                    run = false; // 0. 이전 메뉴
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }
}