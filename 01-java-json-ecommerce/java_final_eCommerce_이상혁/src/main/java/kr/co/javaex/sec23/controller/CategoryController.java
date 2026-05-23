package kr.co.javaex.sec23.controller;

import kr.co.javaex.sec23.service.CategoryService;
import kr.co.javaex.sec23.util.common.InputUtil;

public class CategoryController {

    private final CategoryService categoryService = new CategoryService();

    /**
     * <h3>카테고리 메뉴</h3>
     */
    public void menu() {
        boolean run = true;

        while (run) {
            System.out.println("\n========== 카테고리 관리 ==========");
            System.out.println("1. 카테고리 목록 조회");
            System.out.println("2. 카테고리 등록");
            System.out.println("3. 카테고리 수정");
            System.out.println("4. 카테고리 삭제");
            System.out.println("0. 이전 메뉴");
            System.out.println("==================================");

            int menu = InputUtil.inputInt("메뉴 선택 >> ");

            switch (menu) {
                case 1:
                    categoryService.showAllCategories(); // 카테고리 목록 조회
                    break;
                case 2:
                    categoryService.addCategory(); // 카테고리 등록
                    break;
                case 3:
                    categoryService.updateCategory(); // 카테고리 수정
                    break;
                case 4:
                    categoryService.deleteCategory(); // 카테고리 삭제
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