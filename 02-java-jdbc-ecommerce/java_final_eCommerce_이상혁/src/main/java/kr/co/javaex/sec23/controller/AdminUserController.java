package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.AdminUserService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class AdminUserController {

    private final AdminUserService adminUserService = new AdminUserService();

    public void menu() {
        boolean run = true;

        while (run) {
            System.out.println("\n===== 관리자 회원 관리 =====");
            System.out.println("1. 전체 회원 조회");
            System.out.println("2. 회원 승인");
            System.out.println("3. 탈퇴 요청 처리");
            System.out.println("4. 권한 변경");
            System.out.println("0. 이전 메뉴");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    adminUserService.showAllUsers(); // 1. 전체 회원 조회
                    break;
                case 2:
                    adminUserService.approveUser(); // 2. 회원 승인
                    break;
                case 3:
                    adminUserService.processWithdraw(); // 3. 탈퇴 요청 처리
                    break;
                case 4:
                    adminUserService.changeUserRole(); // 4. 권한 변경
                    break;
                case 0:
                    run = false; // 0. 이전 메뉴
                    break;
                default:
                    System.out.println("잘못된 번호입니다.");
            }
        }
    }
}