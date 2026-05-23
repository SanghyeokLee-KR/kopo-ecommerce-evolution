package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Category;
import kr.co.javaex.sec23.repository.CategoryRepository;
import kr.co.javaex.sec23.util.common.InputUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <h3>카테고리관리 기능</h3>
 *
 * <p>
 * - 카테고리 생성, 수정, 삭제( 관리자 계정만 가능 ) </br>
 *  항목: 카테고리ID, 상위카테고리ID, 카테고리명, 정렬순번 </br>
 *  대분류 / 중분류 구성(카테고리ID, 상위카테고리ID: null이면 대분류) </br>
 *  카테고리별 정렬 순서 설정(순서)
 * </p>
 */
public class CategoryService {

    private final CategoryRepository categoryRepository = new CategoryRepository();

    /**
     * <h3>전체 카테고리 목록 조회</h3>
     */
    public void showAllCategories() {

        // 카테고리 json파일 저장
        List<Category> categories = categoryRepository.findAll();


        // 카테고리가 존재하는지
        if (categories.isEmpty()) {
            System.out.println("카테고리가 없습니다.");
            return;
        }

        List<Category> parentCategories = new ArrayList<>();

        // 대분류만 먼저 추출
        for (Category category : categories) {
            // parentCategoryId 값이 없으면 대분류
            if (category.getParentCategoryId() == null) {
                parentCategories.add(category);
            }
        }

        // 카테고리 -> 정렬순서
        parentCategories.sort(Comparator.comparingInt(Category::getSortOrder));

        System.out.println("\n===== 카테고리 목록 =====");

        for (Category parentCategory : parentCategories) {
            System.out.println(parentCategory.getCategoryName() + " (" + parentCategory.getCategoryId() + ", 정렬순서: " + parentCategory.getSortOrder() + ")");

            List<Category> childCategories = new ArrayList<>();

            // 현재 대분류에 속한 중분류 목록 추출
            for (Category category : categories) {
                if (parentCategory.getCategoryId().equals(category.getParentCategoryId())) {
                    childCategories.add(category);
                }
            }

            // 카테고리 -> 정렬순서
            childCategories.sort(Comparator.comparingInt(Category::getSortOrder));

            // 위에 반환된거 출력
            for (Category childCategory : childCategories) {
                System.out.println("  └ " + childCategory.getCategoryName() + " (" + childCategory.getCategoryId() + ", 정렬순서: " + childCategory.getSortOrder() + ")");
            }
        }
    }

    /**
     * <h3>카테고리 생성</h3>
     * 항목: 카테고리ID, 상위카테고리ID, 카테고리명, 정렬순번
     */
    public void addCategory() {
        System.out.println("\n===== 카테고리 등록 =====");

        String categoryId = InputUtil.inputRequiredLine("카테고리 ID 입력: ");

        // 카테고리 id 있는지 확인
        if (categoryRepository.findById(categoryId) != null) {
            System.out.println("이미 존재하는 카테고리 ID입니다.");
            return;
        }

        System.out.println("1. 대분류");
        System.out.println("2. 중분류");
        int categoryType = InputUtil.inputInt("카테고리 종류 선택: ");

        String parentCategoryId = null;

        switch (categoryType) {
            case 1:
                // 1번일경우 null이 들어가 대분류로 분류
                parentCategoryId = null;
                break;
            case 2:
                // 2번일 경우 selectParentCategoryId() 메서드 실행
                // 중분류 만드는 메서드
                parentCategoryId = selectParentCategoryId();
                if (parentCategoryId == null) {
                    return;
                }
                break;
            default:
                System.out.println("잘못된 번호입니다.");
                return;
        }

        String categoryName = InputUtil.inputRequiredLine("카테고리명 입력: ");
        // 번호 순대로
        int sortOrder = InputUtil.inputPositiveInt("정렬 순서 입력: ");

        List<Category> categories = categoryRepository.findAll();
        categories.add(new Category(categoryId, parentCategoryId, categoryName, sortOrder));

        categoryRepository.saveAll(categories);
        System.out.println("카테고리 등록 완료");
    }

    /**
     * <h3>카테고리 수정</h3>
     * 기존 카테고리의 이름, 상위 카테고리, 정렬 순서 수정
     */
    public void updateCategory() {
        System.out.println("\n===== 카테고리 수정 =====");

        String categoryId = InputUtil.inputRequiredLine("수정할 카테고리 ID 입력: ");
        List<Category> categories = categoryRepository.findAll();

        Category targetCategory = null;

        for (Category category : categories) {
            if (category.getCategoryId().equals(categoryId)) {
                targetCategory = category;
                break;
            }
        }

        if (targetCategory == null) {
            System.out.println("카테고리가 없습니다.");
            return;
        }

        System.out.println("1. 대분류");
        System.out.println("2. 중분류");
        int categoryType = InputUtil.inputInt("카테고리 종류 선택: ");

        String parentCategoryId = null;

        switch (categoryType) {
            case 1:
                parentCategoryId = null;
                break;
            case 2:
                parentCategoryId = selectParentCategoryId();
                if (parentCategoryId == null) {
                    return;
                }
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                return;
        }

        String categoryName = InputUtil.inputRequiredLine("새 카테고리명: ");
        int sortOrder = InputUtil.inputPositiveInt("새 정렬 순서: ");

        targetCategory.setCategoryName(categoryName);
        targetCategory.setParentCategoryId(parentCategoryId);
        targetCategory.setSortOrder(sortOrder);

        categoryRepository.saveAll(categories);
        System.out.println("수정 완료");
    }

    /**
     * <h3>카테고리 삭제</h3>
     * 하위 카테고리 있으면 삭제 불가
     */
    public void deleteCategory() {
        System.out.println("\n===== 카테고리 삭제 =====");

        String categoryId = InputUtil.inputRequiredLine("삭제할 카테고리 ID 입력: ");

        // json파일을 전체 가져와서
        List<Category> categories = categoryRepository.findAll();

        Category targetCategory = null;

        // 삭제할 카테고리 아이디가 입력 값과 일치하는지
        for (Category category : categories) {
            if (category.getCategoryId().equals(categoryId)) {
                targetCategory = category;
                break;
            }
        }

        // 이상한 값이면 메서드 탈출
        if (targetCategory == null) {
            System.out.println("카테고리가 없습니다.");
            return;
        }

        // 하위 카테고리가 남아 있으면 삭제 불가
        for (Category category : categories) {
            if (categoryId.equals(category.getParentCategoryId())) {
                System.out.println("- 하위 카테고리가 있어 삭제 불가 -");
                return;
            }
        }

        categories.remove(targetCategory);
        categoryRepository.saveAll(categories);

        System.out.println("삭제 완료");
    }

    /**
     * <h3>중분류 등록</h3>
     * 중분류 만들고 -> 상위 카테고리 선택
     */
    private String selectParentCategoryId() {
        List<Category> categories = categoryRepository.findAll();
        List<Category> parentCategories = new ArrayList<>();

        // 상위 카테고리 선택 대상은 대분류만 사용
        for (Category category : categories) {
            if (category.getParentCategoryId() == null) {
                parentCategories.add(category);
            }
        }

        // 대분류 있는지 확인
        if (parentCategories.isEmpty()) {
            System.out.println("먼저 대분류를 등록해야 합니다.");
            return null;
        }

        // 카테고리 -> 정렬순서
        parentCategories.sort(Comparator.comparingInt(Category::getSortOrder));

        /*
          ===== 상위 카테고리 선택 =====
          1. 전자제품 (C001, 정렬순서: 1)
          2. 생활용품 (C002, 정렬순서: 2)
          3. 사무용품 (D004, 정렬순서: 3)
          4. 사람 (E005, 정렬순서: 4)
         */
        System.out.println("\n===== 상위 카테고리 선택 =====");
        for (int i = 0; i < parentCategories.size(); i++) {
            Category parentCategory = parentCategories.get(i);
            System.out.println((i + 1) + ". " + parentCategory.getCategoryName() + " (" + parentCategory.getCategoryId() + ", 정렬순서: " + parentCategory.getSortOrder() + ")");
        }

        int choice = InputUtil.inputInt("번호 선택: ");

        if (choice < 1 || choice > parentCategories.size()) {
            System.out.println("잘못된 번호입니다.");
            return null;
        }

        return parentCategories.get(choice - 1).getCategoryId();
    }
}