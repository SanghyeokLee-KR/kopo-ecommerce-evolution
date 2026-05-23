package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Category;
import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.domain.ProductCategoryMapping;
import kr.co.javaex.sec23.repository.CategoryRepository;
import kr.co.javaex.sec23.repository.ProductCategoryMappingRepository;
import kr.co.javaex.sec23.repository.ProductRepository;
import kr.co.javaex.sec23.util.common.InputUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <h3>카테고리 매핑 관리 서비스</h3>
 * - 항목 조회 등록 수정 삭제
 */
public class ProductCategoryMappingService {

    private final ProductCategoryMappingRepository mappingRepository = new ProductCategoryMappingRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();

    /**
     * 전체 상품-카테고리 매핑 목록을 조회
     */
    public void showAllMappings() {
        List<ProductCategoryMapping> mappings = mappingRepository.findAll();

        if (mappings.isEmpty()) {
            System.out.println("등록된 매핑이 없습니다.");
            return;
        }



        // 카테고리 -> 정렬순서 기준 정렬
        mappings.sort(Comparator.comparingInt(ProductCategoryMapping::getSortOrder));



        System.out.println("\n===== 상품-카테고리 매핑 목록 =====");

        for (ProductCategoryMapping mapping : mappings) {
            System.out.println("상품ID : " + mapping.getProductId());
            System.out.println("카테고리ID : " + mapping.getCategoryId());
            System.out.println("정렬순서 : " + mapping.getSortOrder());
            System.out.println("------------------------");
        }
    }

    /**
     * <h3>상품과 중분류 카테고리를 연결하는 매핑 작성</h3>
     */
    public void addMapping() {
        System.out.println("\n===== 상품-카테고리 매핑 등록 =====");

        String productId = selectProductId();
        if (productId == null) {
            return;
        }

        String categoryId = selectChildCategoryId();
        if (categoryId == null) {
            return;
        }

        List<ProductCategoryMapping> mappings = mappingRepository.findAll();

        // 이미 똑같은 매칭이면 메서드 빠져나감
        for (ProductCategoryMapping mapping : mappings) {
            if (mapping.getProductId().equals(productId) && mapping.getCategoryId().equals(categoryId)) {
                System.out.println("- 이미 등록된 매핑입니다. -");
                return;
            }
        }

        int sortOrder = InputUtil.inputPositiveInt("정렬 순서 입력: ");

        ProductCategoryMapping newMapping = new ProductCategoryMapping(productId, categoryId, sortOrder);
        mappingRepository.save(newMapping);

        System.out.println("상품 카테고리 매핑 완료");
    }

    /**
     * <h3>매핑 등록 순서 변경</h3>
     */
    public void updateMapping() {
        System.out.println("\n===== 상품-카테고리 매핑 수정 =====");

        List<ProductCategoryMapping> mappings = mappingRepository.findAll();

        // 존재하는가
        if (mappings.isEmpty()) {
            System.out.println("매핑이 존재 하지 않습니다.");
            return;
        }

        // 정렬
        mappings.sort(Comparator.comparingInt(ProductCategoryMapping::getSortOrder));

        /*
            ===== 상품-카테고리 매핑 수정 =====
            1. 상품ID: P001, 카테고리ID: C101, 정렬순서: 1
            2. 상품ID: P002, 카테고리ID: C101, 정렬순서: 2
            3. 상품ID: P003, 카테고리ID: C101, 정렬순서: 3
            4. 상품ID: P004, 카테고리ID: C101, 정렬순서: 4
            5. 상품ID: P005, 카테고리ID: C101, 정렬순서: 5
            6. 상품ID: P006, 카테고리ID: C102, 정렬순서: 1
         */
        for (int i = 0; i < mappings.size(); i++) {
            ProductCategoryMapping mapping = mappings.get(i);
            System.out.println((i + 1) + ". 상품ID: " + mapping.getProductId() + ", 카테고리ID: " + mapping.getCategoryId() + ", 정렬순서: " + mapping.getSortOrder());
        }

        int choice = InputUtil.inputInt("수정할 번호 선택: ");

        if (choice < 1 || choice > mappings.size()) {
            System.out.println("잘못된 번호입니다.");
            return;
        }

        // -1하는 이유는 보일때 + 1해서 (0번 방지)
        ProductCategoryMapping targetMapping = mappings.get(choice - 1);
        int sortOrder = InputUtil.inputPositiveInt("새 정렬 순서 입력: ");

        targetMapping.setSortOrder(sortOrder);

        mappingRepository.updateAll(mappings);
        System.out.println("매핑 수정 완료");
    }

    /**
     * <h3>매핑 삭제</h3>>
     */
    public void deleteMapping() {
        System.out.println("\n===== 상품-카테고리 매핑 삭제 =====");

        List<ProductCategoryMapping> mappings = mappingRepository.findAll();

        if (mappings.isEmpty()) {
            System.out.println("삭제할 매핑이 없습니다.");
            return;
        }

        mappings.sort(Comparator.comparingInt(ProductCategoryMapping::getSortOrder));


        /*
                ===== 상품-카테고리 매핑 삭제 =====
            1. 상품ID: P001, 카테고리ID: C101, 정렬순서: 1
            2. 상품ID: P002, 카테고리ID: C101, 정렬순서: 2
            3. 상품ID: P003, 카테고리ID: C101, 정렬순서: 3
            4. 상품ID: P004, 카테고리ID: C101, 정렬순서: 4
            5. 상품ID: P005, 카테고리ID: C101, 정렬순서: 5
            6. 상품ID: P006, 카테고리ID: C102, 정렬순서: 1
        */
        for (int i = 0; i < mappings.size(); i++) {
            ProductCategoryMapping mapping = mappings.get(i);
            System.out.println((i + 1) + ". 상품ID: " + mapping.getProductId() + ", 카테고리ID: " + mapping.getCategoryId() + ", 정렬순서: " + mapping.getSortOrder());
        }

        int choice = InputUtil.inputInt("삭제할 번호 선택: ");

        if (choice < 1 || choice > mappings.size()) {
            System.out.println("잘못된 번호입니다.");
            return;
        }

        mappings.remove(choice - 1);
        mappingRepository.updateAll(mappings);

        System.out.println("매핑 삭제 완료");
    }

    /**
     * <h3>상픔 선택 메서드</h3>
     */
    private String selectProductId() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            System.out.println("먼저 상품을 등록해야 합니다.");
            return null;
        }

        System.out.println("\n===== 상품 선택 =====");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product.getProductName() + " (" + product.getProductId() + ")");
        }

        int choice = InputUtil.inputInt("번호 선택: ");

        if (choice < 1 || choice > products.size()) {
            System.out.println("잘못된 번호입니다.");
            return null;
        }

        return products.get(choice - 1).getProductId();
    }

    /**
     * <h3>중분류 카테고리 목록에서 카테고리 선택</h3>
     */
    private String selectChildCategoryId() {
        List<Category> categories = categoryRepository.findAll();
        List<Category> childCategories = new ArrayList<>();

        // 상품 매핑은 중분류 카테고리만 대상으로 사용
        for (Category category : categories) {
            if (category.getParentCategoryId() != null) {
                childCategories.add(category);
            }
        }

        if (childCategories.isEmpty()) {
            System.out.println("먼저 중분류 카테고리를 등록해야 합니다.");
            return null;
        }

        childCategories.sort(Comparator.comparingInt(Category::getSortOrder));

        /*
                ===== 상품-카테고리 매핑 삭제 =====
            1. 상품ID: P001, 카테고리ID: C101, 정렬순서: 1
            2. 상품ID: P002, 카테고리ID: C101, 정렬순서: 2
            3. 상품ID: P003, 카테고리ID: C101, 정렬순서: 3
            4. 상품ID: P004, 카테고리ID: C101, 정렬순서: 4
            5. 상품ID: P005, 카테고리ID: C101, 정렬순서: 5
            6. 상품ID: P006, 카테고리ID: C102, 정렬순서: 1
        */
        System.out.println("\n===== 중분류 카테고리 선택 =====");
        for (int i = 0; i < childCategories.size(); i++) {
            Category category = childCategories.get(i);
            System.out.println((i + 1) + ". " + category.getCategoryName() + " (" + category.getCategoryId() + ", 정렬순서: " + category.getSortOrder() + ")");
        }

        int choice = InputUtil.inputInt("번호 선택: ");

        if (choice < 1 || choice > childCategories.size()) {
            System.out.println("잘못된 번호입니다.");
            return null;
        }

        return childCategories.get(choice - 1).getCategoryId();
    }
}