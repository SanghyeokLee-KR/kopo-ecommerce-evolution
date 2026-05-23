package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.UserService;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.user.UserRole;

/**
 * <h3>유저 컨트롤러</h3>
 * <p>게스트 메뉴, 사용자 메뉴, 어드민 메뉴</p>
 */
public class UserController {

    private final UserService userService;

    // 이 클래스로 넘어오면 새로운 객체가 생성되기에 세션 관리가 안되서 main쪽에 한번만 서비스가 가도록 했습니다.

    /**
     * 이 클래스로 넘어오면 새로운 객체가 생성되기에<br>
     * 세션 관리가 안되서 main쪽에 한번만 서비스가 가도록 했습니다.
     *
     * @param userService
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * <h3>게스트 메뉴</h3>
     * <p>로그인 안되여있을시 보여지는 화면</p>
     */
    public void showGuestMenu() {
        boolean run = true;

        while (run) {
            if (userService.getLoginUser() == null) {
                System.out.println("==============================");
                System.out.println("1. 로그인");
                System.out.println("2. 회원가입");
                System.out.println("0. 종료");
                System.out.println("==============================");

                int menu = InputUtil.inputInt("메뉴 선택 >> ");

                switch (menu) {
                    case 1:
                        boolean success = userService.login();

                        // 유저의 권한이 관리자일 경우 운영자 메뉴 아니면 사용자 메뉴
                        if (success) {
                            if (userService.getLoginUser().getUserRole() == UserRole.관리자) {
                                showAdminMenu(); // 운영자 메뉴
                            } else {
                                showUserMenu(); // 사용자 메뉴
                            }
                        }
                        break;

                    case 2:
                        userService.signUp(); // 회원가입
                        break;

                    case 0:
                        System.out.println("프로그램 종료");
                        run = false;
                        break;

                    default:
                        System.out.println("잘못된 번호입니다");
                }
            } else {
                // 아이디 세션이 null이 아닐 경우 관리자 메뉴로
                if (userService.getLoginUser().getUserRole() == UserRole.관리자) {
                    showAdminMenu();
                } else {
                    // 사용자 메뉴
                    showUserMenu();
                }
            }
        }
    }

    /**
     * <h3>사용자 메뉴</h3>
     * <p>로그인 되여 있으면 나오는 메뉴</p>
     */
    private void showUserMenu() {
        boolean run = true;
        UserProductController userProductController = new UserProductController();
        CartController cartController = new CartController(userService);
        OrderController orderController = new OrderController(userService);

        while (run) {
            System.out.println("\n===== 사용자 메뉴 =====");
            System.out.println("1. 내 정보 조회");
            System.out.println("2. 회원정보 수정");
            System.out.println("3. 비밀번호 변경");
            System.out.println("4. 상품 보기");
            System.out.println("5. 장바구니");
            System.out.println("6. 주문");
            System.out.println("7. 탈퇴 요청");
            System.out.println("8. 로그아웃");
            System.out.println("0. 이전");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    userService.showMyInfo(); // 내 정보 조회
                    break;
                case 2:
                    userService.updateMyInfo(); // 회원정보 수정
                    break;
                case 3:
                    userService.changePassword(); // 비밀번호 번경
                    break;
                case 4:
                    userProductController.menu(); // 상품 메뉴로 이동
                    break;
                case 5:
                    cartController.menu(); // 장바구니 메뉴로 이동
                    break;
                case 6:
                    orderController.menu(); // 주문 메뉴로 이동
                    break;
                case 7:
                    userService.requestWithdraw(); // 탈퇴 요청
                    run = false;
                    break;
                case 8:
                    userService.logout(); // 로그아웃
                    run = false;
                    break;
                case 0:
                    run = false; // 뒤로가기
                    break;
                default:
                    System.out.println("잘못된 번호입니다.");
            }
        }
    }

    /**
     * <h3>운영자 메뉴</h3>
     * <p>어드민 아이디일시 나오는 메뉴</p>
     */
    private void showAdminMenu() {
        boolean run = true;
        CategoryController categoryController = new CategoryController();
        ProductController productController = new ProductController();
        ProductCategoryMappingController mappingController = new ProductCategoryMappingController();
        AdminOrderController adminOrderController = new AdminOrderController();
        AdminUserController adminUserController = new AdminUserController();

        while (run) {
            System.out.println("\n===== 관리자 메뉴 =====");
            System.out.println("1. 카테고리 관리");
            System.out.println("2. 상품 관리");
            System.out.println("3. 상품-카테고리 매핑 관리");
            System.out.println("4. 주문 관리");
            System.out.println("5. 회원 관리");
            System.out.println("6. 로그아웃");
            System.out.println("0. 이전");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    categoryController.menu(); // 카테고리 관리 메뉴
                    break;
                case 2:
                    productController.menu(); // 상품 관리 메뉴
                    break;
                case 3:
                    mappingController.menu(); // 매핑 메뉴
                    break;
                case 4:
                    adminOrderController.menu(); // 주문 관리 메뉴
                    break;
                case 5:
                    adminUserController.menu(); // 회원 메뉴
                    break;
                case 6:
                    userService.logout(); // 로그아웃
                    run = false;
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("잘못된 번호입니다");
            }
        }
    }
}