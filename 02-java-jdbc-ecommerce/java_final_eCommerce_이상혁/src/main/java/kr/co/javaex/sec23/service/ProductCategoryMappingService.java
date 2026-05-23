package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Category;
import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.domain.ProductCategoryMapping;
import kr.co.javaex.sec23.repository.CategoryRepository;
import kr.co.javaex.sec23.repository.ProductCategoryMappingRepository;
import kr.co.javaex.sec23.repository.ProductRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.db.DBUtil;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

public class ProductCategoryMappingService {

    private final ProductCategoryMappingRepository mappingRepository = new ProductCategoryMappingRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();

    // 매핑 목록 조회
    public void showAllMappings() {
        try (Connection con = DBUtil.getConnection()) {
            List<ProductCategoryMapping> mappings = mappingRepository.findAllWithNames(con);

            if (mappings.isEmpty()) {
                System.out.println("등록된 매핑이 없습니다.");
                return;
            }

            System.out.println("\n===== 상품-카테고리 매핑 목록 =====");

            for (ProductCategoryMapping mapping : mappings) {
                System.out.println("매핑번호 : " + mapping.getMappingNo());
                System.out.println("상품번호 : " + mapping.getProductNo());
                System.out.println("상품명 : " + mapping.getProductName());
                System.out.println("카테고리번호 : " + mapping.getCategoryNo());
                System.out.println("카테고리명 : " + mapping.getCategoryName());
                System.out.println("정렬순서 : " + mapping.getSortOrder());
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            System.out.println("매핑 목록 조회 실패: " + e.getMessage());
            throw new RuntimeException("매핑 목록 조회 실패", e);
        }
    }

    // 매핑 등록
    public void addMapping() {
        System.out.println("\n===== 상품-카테고리 매핑 등록 =====");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Integer productNo = selectProductNo(con);
            if (productNo == null) {
                return;
            }

            Integer categoryNo = selectChildCategoryNo(con);
            if (categoryNo == null) {
                return;
            }

            if (mappingRepository.existsMapping(con, productNo, categoryNo)) {
                System.out.println("- 이미 등록된 매핑입니다. -");
                return;
            }

            int sortOrder = InputUtil.inputPositiveInt("정렬 순서 입력: ");

            ProductCategoryMapping newMapping = ProductCategoryMapping.builder()
                    .productNo(productNo)
                    .categoryNo(categoryNo)
                    .sortOrder(sortOrder)
                    .build();

            mappingRepository.save(con, newMapping);

            con.commit();
            System.out.println("상품 카테고리 매핑 완료");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("매핑 등록 실패: " + e.getMessage());
            throw new RuntimeException("매핑 등록 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 매핑 수정
    public void updateMapping() {
        System.out.println("\n===== 상품-카테고리 매핑 수정 =====");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            List<ProductCategoryMapping> mappings = mappingRepository.findAllWithNames(con);

            if (mappings.isEmpty()) {
                System.out.println("매핑이 존재하지 않습니다.");
                return;
            }

            mappings.sort(Comparator.comparingInt(ProductCategoryMapping::getSortOrder)
                    .thenComparingInt(ProductCategoryMapping::getMappingNo));

            for (int i = 0; i < mappings.size(); i++) {
                ProductCategoryMapping mapping = mappings.get(i);

                System.out.println((i + 1) + ". 매핑번호: " + mapping.getMappingNo()
                        + ", 상품명: " + mapping.getProductName()
                        + ", 카테고리명: " + mapping.getCategoryName()
                        + ", 정렬순서: " + mapping.getSortOrder());
            }

            int choice = InputUtil.inputInt("수정할 번호 선택: ");

            if (choice < 1 || choice > mappings.size()) {
                System.out.println("잘못된 번호입니다.");
                return;
            }

            ProductCategoryMapping targetMapping = mappings.get(choice - 1);
            int newSortOrder = InputUtil.inputPositiveInt("새 정렬 순서 입력: ");

            mappingRepository.updateSortOrder(con, targetMapping.getMappingNo(), newSortOrder);

            con.commit();
            System.out.println("매핑 수정 완료");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("매핑 수정 실패: " + e.getMessage());
            throw new RuntimeException("매핑 수정 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 매핑 삭제
    public void deleteMapping() {
        System.out.println("\n===== 상품-카테고리 매핑 삭제 =====");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            List<ProductCategoryMapping> mappings = mappingRepository.findAllWithNames(con);

            if (mappings.isEmpty()) {
                System.out.println("삭제할 매핑이 없습니다.");
                return;
            }

            mappings.sort(Comparator.comparingInt(ProductCategoryMapping::getSortOrder)
                    .thenComparingInt(ProductCategoryMapping::getMappingNo));

            for (int i = 0; i < mappings.size(); i++) {
                ProductCategoryMapping mapping = mappings.get(i);

                System.out.println((i + 1) + ". 매핑번호: " + mapping.getMappingNo()
                        + ", 상품명: " + mapping.getProductName()
                        + ", 카테고리명: " + mapping.getCategoryName()
                        + ", 정렬순서: " + mapping.getSortOrder());
            }

            int choice = InputUtil.inputInt("삭제할 번호 선택: ");

            if (choice < 1 || choice > mappings.size()) {
                System.out.println("잘못된 번호입니다.");
                return;
            }

            ProductCategoryMapping targetMapping = mappings.get(choice - 1);

            mappingRepository.deleteByMappingNo(con, targetMapping.getMappingNo());

            con.commit();
            System.out.println("매핑 삭제 완료");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("매핑 삭제 실패: " + e.getMessage());
            throw new RuntimeException("매핑 삭제 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    private Integer selectProductNo(Connection con) throws Exception {
        List<Product> products = productRepository.findAll(con);

        if (products.isEmpty()) {
            System.out.println("먼저 상품을 등록해야 합니다.");
            return null;
        }

        System.out.println("\n===== 상품 선택 =====");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product.getProductName()
                    + " (" + product.getProductId() + ", 상품번호: " + product.getProductNo() + ")");
        }

        int choice = InputUtil.inputInt("번호 선택: ");

        if (choice < 1 || choice > products.size()) {
            System.out.println("잘못된 번호입니다.");
            return null;
        }

        return products.get(choice - 1).getProductNo();
    }

    private Integer selectChildCategoryNo(Connection con) throws Exception {
        List<Category> childCategories = categoryRepository.findChildren(con);

        if (childCategories.isEmpty()) {
            System.out.println("먼저 중분류 카테고리를 등록해야 합니다.");
            return null;
        }

        childCategories.sort(Comparator.comparingInt(Category::getSortOrder)
                .thenComparingInt(Category::getCategoryNo));

        System.out.println("\n===== 중분류 카테고리 선택 =====");
        for (int i = 0; i < childCategories.size(); i++) {
            Category category = childCategories.get(i);
            System.out.println((i + 1) + ". " + category.getCategoryName()
                    + " (" + category.getCategoryId()
                    + ", 카테고리번호: " + category.getCategoryNo()
                    + ", 정렬순서: " + category.getSortOrder() + ")");
        }

        int choice = InputUtil.inputInt("번호 선택: ");

        if (choice < 1 || choice > childCategories.size()) {
            System.out.println("잘못된 번호입니다.");
            return null;
        }

        return childCategories.get(choice - 1).getCategoryNo();
    }
}