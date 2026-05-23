package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.UserService;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.user.UserRole;

public class UserController {

    private final UserService userService;

    // 메뉴를 오가도 로그인 상태가 유지되도록 같은 UserService를 공유하기 위해 만들었습니다.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void showGuestMenu() {
        boolean run = true;

        while (run) {
            if (userService.getLoginUser() == null) {
                System.out.println();
                System.out.println("======================================");
                System.out.println("        이상혁의 전자제품 쇼핑몰");
                System.out.println("======================================");
                System.out.println("1. 로그인");
                System.out.println("2. 회원가입");
                System.out.println("0. 프로그램 종료");
                System.out.println("======================================");

                int menu = InputUtil.inputInt("메뉴 선택 >> ");

                switch (menu) {
                    case 1:
                        boolean success = userService.login(); // 1. 로그인

                        if (success) {
                            if (userService.getLoginUser().getUserRole() == UserRole.관리자) {
                                showAdminMenu(); // 관리자 메뉴로 이동
                            } else {
                                showUserMenu(); // 사용자 메뉴로 이동
                            }
                        }
                        break;

                    case 2:
                        userService.signUp(); // 2. 회원가입
                        break;

                    case 0:
                        System.out.println("종료"); // 0. 프로그램 종료
                        run = false;
                        break;

                    default:
                        System.out.println("잘못된 메뉴 번호입니다. 다시 선택해주세요.");
                }
            } else {
                if (userService.getLoginUser().getUserRole() == UserRole.관리자) {
                    showAdminMenu();
                } else {
                    showUserMenu();
                }
            }
        }
    }

    private void showUserMenu() {
        boolean run = true;
        UserProductController userProductController = new UserProductController();
        CartController cartController = new CartController(userService);
        OrderController orderController = new OrderController(userService);

        while (run) {
            System.out.println();
            System.out.println("======================================");
            System.out.println("    이상혁의 전자제품 쇼핑몰 사용자 메뉴");
            System.out.println("======================================");
            System.out.println("1. 내 회원정보 조회");
            System.out.println("2. 회원정보 수정");
            System.out.println("3. 비밀번호 변경");
            System.out.println("4. 전자제품 상품 보기");
            System.out.println("5. 장바구니 관리");
            System.out.println("6. 주문 / 결제");
            System.out.println("7. 회원 탈퇴 요청");
            System.out.println("8. 로그아웃");
            System.out.println("0. 이전 메뉴로");
            System.out.println("======================================");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    userService.showMyInfo(); // 1. 내 회원정보 조회
                    break;

                case 2:
                    userService.updateMyInfo(); // 2. 회원정보 수정
                    break;

                case 3:
                    userService.changePassword(); // 3. 비밀번호 변경
                    break;

                case 4:
                    userProductController.menu(); // 4. 전자제품 상품 보기
                    break;

                case 5:
                    cartController.menu(); // 5. 장바구니 관리
                    break;

                case 6:
                    orderController.menu(); // 6. 주문 / 결제
                    break;

                case 7:
                    userService.requestWithdraw(); // 7. 회원 탈퇴 요청
                    run = false;
                    break;

                case 8:
                    userService.logout(); // 8. 로그아웃
                    run = false;
                    break;

                case 0:
                    run = false; // 0. 이전 메뉴로
                    break;

                default:
                    System.out.println("잘못된 메뉴 번호입니다. 다시 선택해주세요.");
            }
        }
    }

    private void showAdminMenu() {
        boolean run = true;
        CategoryController categoryController = new CategoryController();
        ProductController productController = new ProductController();
        ProductCategoryMappingController mappingController = new ProductCategoryMappingController();
        AdminOrderController adminOrderController = new AdminOrderController();
        AdminUserController adminUserController = new AdminUserController();

        while (run) {
            System.out.println();
            System.out.println("======================================");
            System.out.println("              관리자 메뉴");
            System.out.println("======================================");
            System.out.println("1. 카테고리 관리");
            System.out.println("2. 전자제품 상품 관리");
            System.out.println("3. 상품-카테고리 매핑 관리");
            System.out.println("4. 주문 내역 관리");
            System.out.println("5. 회원 관리");
            System.out.println("6. 로그아웃");
            System.out.println("0. 이전 메뉴로");
            System.out.println("======================================");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    categoryController.menu(); // 1. 카테고리 관리
                    break;

                case 2:
                    productController.menu(); // 2. 전자제품 상품 관리
                    break;

                case 3:
                    mappingController.menu(); // 3. 상품-카테고리 매핑 관리
                    break;

                case 4:
                    adminOrderController.menu(); // 4. 주문 내역 관리
                    break;

                case 5:
                    adminUserController.menu(); // 5. 회원 관리
                    break;

                case 6:
                    userService.logout(); // 6. 로그아웃
                    run = false;
                    break;

                case 0:
                    run = false; // 0. 이전 메뉴로
                    break;

                default:
                    System.out.println("잘못된 메뉴 번호입니다. 다시 선택해주세요.");
            }
        }
    }
}