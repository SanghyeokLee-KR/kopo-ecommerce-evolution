package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.ProductService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class ProductController {

    private final ProductService productService = new ProductService();

    public void menu() {
        boolean run = true;

        while (run) {

            System.out.println();
            System.out.println("======================================");
            System.out.println("      전자제품 상품 관리 메뉴");
            System.out.println("======================================");
            System.out.println("1. 전체 상품 목록 조회");
            System.out.println("2. 상품 상세 정보 조회");
            System.out.println("3. 신규 전자제품 등록");
            System.out.println("4. 상품 정보 수정");
            System.out.println("5. 상품 삭제");
            System.out.println("6. 판매 중지 처리");
            System.out.println("7. 재고 수량 변경");
            System.out.println("0. 이전 메뉴");
            System.out.println("======================================");

            int menu = InputUtil.inputInt("메뉴 선택 >> ");

            switch (menu) {
                case 1:
                    productService.showAllProducts(); // 1. 전체 상품 목록 조회
                    break;
                case 2:
                    productService.showProductDetail(); // 2. 상품 상세 정보 조회
                    break;
                case 3:
                    productService.addProduct(); // 3. 신규 전자제품 등록
                    break;
                case 4:
                    productService.updateProduct(); // 4. 상품 정보 수정
                    break;
                case 5:
                    productService.deleteProduct(); // 5. 상품 삭제
                    break;
                case 6:
                    productService.stopSellingProduct(); // 6. 판매 중지 처리
                    break;
                case 7:
                    productService.updateStock(); // 7. 재고 수량 변경
                    break;
                case 0:
                    run = false; // 0. 이전 메뉴
                    break;
                default:
                    System.out.println("잘못된 메뉴 번호입니다. 다시 선택해주세요.");
            }
        }
    }
}