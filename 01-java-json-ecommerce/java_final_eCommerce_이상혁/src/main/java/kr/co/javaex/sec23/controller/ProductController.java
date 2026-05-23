package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.ProductService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class ProductController {

    private final ProductService productService = new ProductService();

    /**
     * <h3>상품관리 메뉴</h3>
     */
    public void menu() {
        boolean run = true;

        while (run) {
            System.out.println("\n========== 상품 관리 ==========");
            System.out.println("1. 상품 목록 조회");
            System.out.println("2. 상품 상세 조회");
            System.out.println("3. 상품 등록");
            System.out.println("4. 상품 수정");
            System.out.println("5. 상품 삭제");
            System.out.println("6. 판매중지 처리");
            System.out.println("7. 재고 수정");
            System.out.println("0. 이전 메뉴");
            System.out.println("==============================");

            int menu = InputUtil.inputInt("메뉴 선택 >> ");

            switch (menu) {
                case 1:
                    productService.showAllProducts(); // 상품 목록 조회
                    break;
                case 2:
                    productService.showProductDetail(); // 상품 상세 조회
                    break;
                case 3:
                    productService.addProduct(); // 상품 등록
                    break;
                case 4:
                    productService.updateProduct(); // 상품 수정
                    break;
                case 5:
                    productService.deleteProduct(); // 상품 삭제
                    break;
                case 6:
                    productService.stopSellingProduct(); // 판매중지 처리
                    break;
                case 7:
                    productService.updateStock(); // 재고 수정
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("잘못된 메뉴 번호입니다.");
            }
        }
    }
}