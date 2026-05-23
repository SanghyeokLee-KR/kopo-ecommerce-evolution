package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.ProductCategoryMappingService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class ProductCategoryMappingController {

    private final ProductCategoryMappingService mappingService = new ProductCategoryMappingService();

    public void menu() {
        boolean run = true;

        while (run) {
            System.out.println("\n========== 상품-카테고리 매핑 관리 ==========");
            System.out.println("1. 매핑 목록 조회");
            System.out.println("2. 매핑 등록");
            System.out.println("3. 매핑 수정");
            System.out.println("4. 매핑 삭제");
            System.out.println("0. 이전 메뉴");
            System.out.println("===========================================");

            int menu = InputUtil.inputInt("메뉴 선택 >> ");

            switch (menu) {
                case 1:
                    mappingService.showAllMappings(); // 1. 매핑 목록 조회
                    break;
                case 2:
                    mappingService.addMapping(); // 2. 매핑 등록
                    break;
                case 3:
                    mappingService.updateMapping(); // 3. 매핑 수정
                    break;
                case 4:
                    mappingService.deleteMapping(); // 4. 매핑 삭제
                    break;
                case 0:
                    run = false; // 0. 이전 메뉴
                    break;
                default:
                    System.out.println("잘못된 메뉴 번호입니다.");
            }
        }
    }
}