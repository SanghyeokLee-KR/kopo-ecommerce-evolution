package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.UserProductService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class UserProductController {

    private final UserProductService userProductService = new UserProductService();

    public void menu() {
        boolean run = true;

        while (run) {

            System.out.println();
            System.out.println("======================================");
            System.out.println("        전자제품 상품 전시 메뉴");
            System.out.println("======================================");
            System.out.println("1. 전체 상품 조회");
            System.out.println("2. 카테고리별 상품 조회");
            System.out.println("3. 가격순 상품 조회");
            System.out.println("4. 상품명 검색");
            System.out.println("5. 상품 상세 정보 조회");
            System.out.println("0. 이전 메뉴");
            System.out.println("======================================");

            int menu = InputUtil.inputInt("선택 >> ");

            switch (menu) {
                case 1:
                    userProductService.showAllProducts(); // 1. 전체 상품 조회
                    break;
                case 2:
                    userProductService.showProductsByCategory(); // 2. 카테고리별 상품 조회
                    break;
                case 3:
                    userProductService.showProductsByPrice(); // 3. 가격순 상품 조회
                    break;
                case 4:
                    userProductService.searchProducts(); // 4. 상품명 검색
                    break;
                case 5:
                    userProductService.showProductDetail(); // 5. 상품 상세 정보 조회
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