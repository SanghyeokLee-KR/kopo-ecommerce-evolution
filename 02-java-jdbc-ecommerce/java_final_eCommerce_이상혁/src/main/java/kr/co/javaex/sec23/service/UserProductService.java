package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Category;
import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.repository.CategoryRepository;
import kr.co.javaex.sec23.repository.ProductRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.ProductStatus.ProductStatus;
import kr.co.javaex.sec23.util.db.DBUtil;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

public class UserProductService {

    private final ProductRepository productRepository = new ProductRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();

    // 전체 상품 조회
    public void showAllProducts() {
        try (Connection con = DBUtil.getConnection()) {
            List<Product> products = productRepository.findAllAvailable(con);

            if (products.isEmpty()) {
                System.out.println("조회 가능한 상품이 없습니다.");
                return;
            }

            System.out.println("\n===== 전체 상품 목록 =====");
            for (Product product : products) {
                printSimpleProduct(product);
            }

        } catch (Exception e) {
            System.out.println("전체 상품 조회 실패: " + e.getMessage());
            throw new RuntimeException("전체 상품 조회 실패", e);
        }
    }

    // 카테고리별 상품 조회
    public void showProductsByCategory() {
        try (Connection con = DBUtil.getConnection()) {
            List<Category> childCategories = categoryRepository.findChildren(con);

            if (childCategories.isEmpty()) {
                System.out.println("중분류 카테고리가 없습니다.");
                return;
            }

            childCategories.sort(Comparator.comparingInt(Category::getSortOrder)
                    .thenComparingInt(Category::getCategoryNo));

            System.out.println("\n===== 카테고리 선택 =====");
            for (int i = 0; i < childCategories.size(); i++) {
                Category category = childCategories.get(i);
                System.out.println((i + 1) + ". " + category.getCategoryName()
                        + " (" + category.getCategoryId()
                        + ", 정렬순서: " + category.getSortOrder() + ")");
            }

            int choice = InputUtil.inputInt("번호 선택: ");

            if (choice < 1 || choice > childCategories.size()) {
                System.out.println("잘못된 번호입니다.");
                return;
            }

            Category selectedCategory = childCategories.get(choice - 1);

            List<Product> products =
                    productRepository.findByCategoryNo(con, selectedCategory.getCategoryNo());

            if (products.isEmpty()) {
                System.out.println("카테고리에 조회 가능한 상품이 없습니다.");
                return;
            }

            System.out.println("\n===== 카테고리별 상품 목록 =====");
            System.out.println("카테고리명 : " + selectedCategory.getCategoryName());

            for (Product product : products) {
                printSimpleProduct(product);
            }

        } catch (Exception e) {
            System.out.println("카테고리별 상품 조회 실패: " + e.getMessage());
            throw new RuntimeException("카테고리별 상품 조회 실패", e);
        }
    }

    // 가격순 상품 조회
    public void showProductsByPrice() {
        try (Connection con = DBUtil.getConnection()) {
            System.out.println("1. 낮은 가격순");
            System.out.println("2. 높은 가격순");
            int choice = InputUtil.inputInt("정렬 방식 선택: ");

            List<Product> products;

            switch (choice) {
                case 1:
                    products = productRepository.findAllOrderByPriceAsc(con);
                    break;
                case 2:
                    products = productRepository.findAllOrderByPriceDesc(con);
                    break;
                default:
                    System.out.println("잘못된 번호입니다.");
                    return;
            }

            if (products.isEmpty()) {
                System.out.println("조회 가능한 상품이 없습니다.");
                return;
            }

            System.out.println("\n===== 가격순 상품 목록 =====");
            for (Product product : products) {
                printSimpleProduct(product);
            }

        } catch (Exception e) {
            System.out.println("가격순 상품 조회 실패: " + e.getMessage());
            throw new RuntimeException("가격순 상품 조회 실패", e);
        }
    }

    // 상품명 검색
    public void searchProducts() {
        String keyword = InputUtil.inputRequiredLine("검색어 입력: ").trim();

        try (Connection con = DBUtil.getConnection()) {
            List<Product> products = productRepository.searchByName(con, keyword);

            if (products.isEmpty()) {
                System.out.println("검색 결과가 없습니다.");
                return;
            }

            System.out.println("\n===== 검색 결과 =====");
            for (Product product : products) {
                printSimpleProduct(product);
            }

        } catch (Exception e) {
            System.out.println("상품 검색 실패: " + e.getMessage());
            throw new RuntimeException("상품 검색 실패", e);
        }
    }

    // 상품 상세 정보 조회
    public void showProductDetail() {
        String productId = InputUtil.inputRequiredLine("조회할 상품 ID 입력: ");

        try (Connection con = DBUtil.getConnection()) {
            Product product = productRepository.findByProductId(con, productId);

            if (product == null) {
                System.out.println("해당 상품이 없습니다.");
                return;
            }

            if (product.getProductStatus() == ProductStatus.판매중지) {
                System.out.println("판매중지된 상품입니다.");
                return;
            }

            System.out.println("\n===== 상품 상세 정보 =====");
            System.out.println("상품번호 : " + product.getProductNo());
            System.out.println("상품ID : " + product.getProductId());
            System.out.println("상품명 : " + product.getProductName());
            System.out.println("상세설명 : " + product.getProductDescription());
            System.out.println("가격 : " + product.getPrice());
            System.out.println("재고수량 : " + product.getStockQuantity());
            System.out.println("상태 : " + product.getProductStatus());

        } catch (Exception e) {
            System.out.println("상품 상세 조회 실패: " + e.getMessage());
            throw new RuntimeException("상품 상세 조회 실패", e);
        }
    }

    private void printSimpleProduct(Product product) {
        System.out.println("상품번호 : " + product.getProductNo());
        System.out.println("상품ID : " + product.getProductId());
        System.out.println("상품명 : " + product.getProductName());
        System.out.println("가격 : " + product.getPrice());
        System.out.println("상태 : " + product.getProductStatus());
        System.out.println("------------------------");
    }
}
