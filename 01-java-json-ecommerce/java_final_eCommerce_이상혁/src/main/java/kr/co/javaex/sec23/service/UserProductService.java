package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Category;
import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.domain.ProductCategoryMapping;
import kr.co.javaex.sec23.repository.CategoryRepository;
import kr.co.javaex.sec23.repository.ProductCategoryMappingRepository;
import kr.co.javaex.sec23.repository.ProductRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.ProductStatus.ProductStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Integer.reverse;

/**
 * <h3>사용자용 상품 전시 기능을 처리하는 서비스</h3>
 * - 전체 상품 조회
 * - 카테고리별 조회
 * - 가격순 조회
 * - 상품명 검색
 * - 상품 상세 조회
 */
public class UserProductService {

    private final ProductRepository productRepository = new ProductRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final ProductCategoryMappingRepository mappingRepository = new ProductCategoryMappingRepository();

    /**
     * 판매중지되지 않은 전체 상품 목록을 조회
     */
    public void showAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            System.out.println("등록된 상품이 없습니다.");
            return;
        }

        System.out.println("\n===== 전체 상품 목록 =====");

        boolean hasVisibleProduct = false;

        for (Product product : products) {
            if (product.getProductStatus() == ProductStatus.판매중지) {
                continue;
            }

            printSimpleProduct(product);
            hasVisibleProduct = true;
        }

        if (!hasVisibleProduct) {
            System.out.println("조회 가능한 상품이 없습니다.");
        }
    }

    /**
     * 중분류 카테고리를 선택해 해당 상품 목록을 조회
     */
    public void showProductsByCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<Category> childCategories = new ArrayList<>();

        // 사용자 조회용 카테고리는 중분류만 사용
        for (Category category : categories) {
            if (category.getParentCategoryId() != null) {
                childCategories.add(category);
            }
        }

        if (childCategories.isEmpty()) {
            System.out.println("중분류 카테고리가 없습니다.");
            return;
        }

        childCategories.sort(Comparator.comparingInt(Category::getSortOrder));

        System.out.println("\n===== 카테고리 선택 =====");
        for (int i = 0; i < childCategories.size(); i++) {
            Category category = childCategories.get(i);
            System.out.println((i + 1) + ". " + category.getCategoryName() + " (" + category.getCategoryId() + ", 정렬순서: " + category.getSortOrder() + ")");
        }

        int choice = InputUtil.inputInt("번호 선택: ");

        if (choice < 1 || choice > childCategories.size()) {
            System.out.println("잘못된 번호입니다.");
            return;
        }

        Category selectedCategory = childCategories.get(choice - 1);
        String categoryId = selectedCategory.getCategoryId();

        List<ProductCategoryMapping> mappings = mappingRepository.findAll();
        List<ProductCategoryMapping> categoryMappings = new ArrayList<>();

        // 선택한 카테고리에 연결된 상품 매핑만 추출
        for (ProductCategoryMapping mapping : mappings) {
            if (categoryId.equals(mapping.getCategoryId())) {
                categoryMappings.add(mapping);
            }
        }

        if (categoryMappings.isEmpty()) {
            System.out.println("카테고리에 등록된 상품이 없습니다.");
            return;
        }
        categoryMappings.sort(Comparator.comparingInt(ProductCategoryMapping::getSortOrder));

        List<Product> filteredProducts = new ArrayList<>();

        // 매핑된 상품 중 판매중지되지 않은 상품만 조회 대상으로 사용
        for (ProductCategoryMapping mapping : categoryMappings) {
            Product product = productRepository.findById(mapping.getProductId());

            if (product != null && product.getProductStatus() != ProductStatus.판매중지) {
                filteredProducts.add(product);
            }
        }

        if (filteredProducts.isEmpty()) {
            System.out.println("카테고리에 조회 가능한 상품이 없습니다.");
            return;
        }

        System.out.println("\n===== 카테고리별 상품 목록 =====");
        System.out.println("카테고리명 : " + selectedCategory.getCategoryName());

        for (Product product : filteredProducts) {
            printSimpleProduct(product);
        }
    }

    /**
     * 가격 기준으로 상품 목록을 정렬해 조회
     */
    public void showProductsByPrice() {
        List<Product> products = productRepository.findAll();
        List<Product> filteredProducts = new ArrayList<>();

        // 판매중지 상품은 사용자 목록에서 제외
        for (Product product : products) {
            if (product.getProductStatus() != ProductStatus.판매중지) {
                filteredProducts.add(product);
            }
        }

        if (filteredProducts.isEmpty()) {
            System.out.println("조회 가능한 상품이 없습니다.");
            return;
        }

        System.out.println("1. 낮은 가격순");
        System.out.println("2. 높은 가격순");
        int choice = InputUtil.inputInt("정렬 방식 선택: ");

        switch (choice) {
            case 1:
                // 낮은 가격순
                filteredProducts.sort(Comparator.comparingInt(Product::getPrice));
                break;
            case 2:
                // 높은 가격순
                filteredProducts.sort(Comparator.comparingInt(Product::getPrice).reversed());
                break;

            default:
                System.out.println("잘못된 번호입니다.");
                return;
        }

        System.out.println("\n===== 가격순 상품 목록 =====");
        for (Product product : filteredProducts) {
            printSimpleProduct(product);
        }
    }

    /**
     * 상품명 키워드로 검색
     */
    public void searchProducts() {
        String keyword = InputUtil.inputRequiredLine("검색어 입력: ").trim();

        List<Product> products = productRepository.findAll();
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.getProductStatus() == ProductStatus.판매중지) {
                continue;
            }

            if (product.getProductName().contains(keyword)) {
                filteredProducts.add(product);
            }
        }

        if (filteredProducts.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
            return;
        }

        System.out.println("\n===== 검색 결과 =====");
        for (Product product : filteredProducts) {
            printSimpleProduct(product);
        }
    }

    /**
     * <h3>상품 ID로 상세 정보를 조회</h3>
     * 판매중지된 상품은 상세 조회 대상에서 제외
     */
    public void showProductDetail() {
        String productId = InputUtil.inputRequiredLine("조회할 상품 ID 입력: ");
        Product product = productRepository.findById(productId);

        if (product == null) {
            System.out.println("해당 상품이 없습니다.");
            return;
        }

        if (product.getProductStatus() == ProductStatus.판매중지) {
            System.out.println("판매중지된 상품입니다.");
            return;
        }

        System.out.println("\n===== 상품 상세 정보 =====");
        System.out.println("상품ID : " + product.getProductId());
        System.out.println("상품명 : " + product.getProductName());
        System.out.println("상세설명 : " + product.getProductDescription());
        System.out.println("가격 : " + product.getPrice());
        System.out.println("재고수량 : " + product.getStockQuantity());
        System.out.println("상태 : " + product.getProductStatus());
    }

    private void printSimpleProduct(Product product) {
        System.out.println("상품ID : " + product.getProductId());
        System.out.println("상품명 : " + product.getProductName());
        System.out.println("가격 : " + product.getPrice());
        System.out.println("상태 : " + product.getProductStatus());
        System.out.println("------------------------");
    }
}