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

/**
 * <h3>상품 관리 서비스</h3>
 * 항목 - 상품 등록, 수정, 삭제, 재고 관리, 카테고리 매핑
 */
public class ProductService {

    private final ProductRepository productRepository = new ProductRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final ProductCategoryMappingRepository mappingRepository = new ProductCategoryMappingRepository();


    /**
     * <h3>상품 조회 기능</h3>
     */
    public void showAllProducts() {

        //  products.json를 리스트로 받아온거 products 저장
        List<Product> products = productRepository.findAll();

        // 있는지 확인
        if (products.isEmpty()) {
            System.out.println("등록된 상품이 없습니다.");
            return;
        }

        System.out.println("\n===== 상품 목록 =====");

        // 전체 출력
        for (Product product : products) {
            printProduct(product);
        }
    }

    /**
     * <h3>상세 정보 조회</h3>
     */
    public void showProductDetail() {
        System.out.println("\n===== 상품 상세 조회 =====");

        String productId = InputUtil.inputRequiredLine("조회할 상품 ID 입력: ");

        // products.json를 id값으로 받아옴
        Product product = productRepository.findById(productId);

        if (product == null) {
            System.out.println("상품 ID를 다시 확인해주세요.");
            return;
        }

        printProduct(product);
    }

    /**
     * 새 상품을 등록, 카테고리 매핑
     */
    public void addProduct() {
        System.out.println("\n===== 상품 등록 =====");

        String productId = InputUtil.inputRequiredLine("상품 ID 입력: ");

        // id가 존재하는지
        if (productRepository.findById(productId) != null) {
            System.out.println("이미 존재하는 상품 ID입니다.");
            return;
        }

        String productName = InputUtil.inputRequiredLine("상품명 입력: ");
        String productDescription = InputUtil.inputRequiredLine("상세 설명 입력: ");
        int price = InputUtil.inputNonNegativeInt("가격 입력: ");
        int stockQuantity = InputUtil.inputNonNegativeInt("재고 수량 입력: ");

        // 재고 수량이 0이면 품절 아니면 정상
        ProductStatus productStatus = (stockQuantity == 0) ? ProductStatus.품절 : ProductStatus.정상;

        // 값 newProduct 인스턴스 경로 참조
        Product newProduct = new Product(productId, productName, productDescription, price, stockQuantity, productStatus);

        // 값을 저장한다.
        productRepository.save(newProduct);
        System.out.println("상품이 등록되었습니다.");

        // 등록 후에 카테고리 지정 메서드로 감
        registerCategoryMapping(productId);
    }

    /**
     * <h3>상품 수정</h3>
     * 항목 - 상품명, 상세 설명, 가격을 수정
     */
    public void updateProduct() {
        System.out.println("\n===== 상품 수정 =====");

        String productId = InputUtil.inputRequiredLine("수정할 상품 ID 입력: ");
        List<Product> products = productRepository.findAll();

        Product targetProduct = findProductOrPrintError(productId, products);
        if (targetProduct == null) {
            return;
        }

        String newProductName = InputUtil.inputRequiredLine("새 상품명 입력: ");
        String newProductDescription = InputUtil.inputRequiredLine("새 상세 설명 입력: ");
        int newPrice = InputUtil.inputNonNegativeInt("새 가격 입력: ");

        targetProduct.setProductName(newProductName);
        targetProduct.setProductDescription(newProductDescription);
        targetProduct.setPrice(newPrice);

        productRepository.updateAll(products);
        System.out.println("상품 정보가 수정되었습니다.");
    }

    /**
     * <h3>상품 삭제</h3>
     * 상품을 삭제하고 연결된 카테고리 매핑도 함께 정리
     */
    public void deleteProduct() {
        System.out.println("\n===== 상품 삭제 =====");

        // json을 리스트로 가져온거 id 반환
        String productId = InputUtil.inputRequiredLine("삭제할 상품 ID 입력: ");
        List<Product> products = productRepository.findAll();


        Product targetProduct = findProductOrPrintError(productId, products);
        if (targetProduct == null) {
            return;
        }

        products.remove(targetProduct);
        productRepository.updateAll(products);

        // 매핑도 같이 삭제
        deleteMappingsByProductId(productId);

        System.out.println("상품이 삭제되었습니다.");
    }

    /**
     * 상품 상태를 판매중지로 변경한다.
     */
    public void stopSellingProduct() {
        System.out.println("\n===== 판매중지 처리 =====");

        String productId = InputUtil.inputRequiredLine("상품 ID 입력: ");
        List<Product> products = productRepository.findAll();

        Product targetProduct = findProductOrPrintError(productId, products);
        if (targetProduct == null) {
            return;
        }

        targetProduct.setProductStatus(ProductStatus.판매중지);
        productRepository.updateAll(products);

        System.out.println("판매중지 처리가 완료되었습니다.");
    }

    /**
     * 재고 수량을 수정하고, 재고 상태를 함께 갱신한다.
     */
    public void updateStock() {
        System.out.println("\n===== 재고 수정 =====");

        String productId = InputUtil.inputRequiredLine("상품 ID 입력: ");
        List<Product> products = productRepository.findAll();

        // id값이랑 상품명 보내서 객체 가져옴
        Product targetProduct = findProductOrPrintError(productId, products);
        if (targetProduct == null) {
            return;
        }

        int newStockQuantity = InputUtil.inputNonNegativeInt("새 재고 수량 입력: ");
        targetProduct.setStockQuantity(newStockQuantity);

        // 수량에 따라 상태를 조정
        if (newStockQuantity == 0) {
            targetProduct.setProductStatus(ProductStatus.품절);
        } else if (targetProduct.getProductStatus() == ProductStatus.품절) {
            targetProduct.setProductStatus(ProductStatus.정상);
        }

        productRepository.updateAll(products);
        System.out.println("재고가 수정되었습니다.");
    }

    /**
     * <h3>상품 등록후 - 카테고리 매핑</h3>
     */
    private void registerCategoryMapping(String productId) {
        System.out.println("1. 카테고리 지정");
        System.out.println("2. 카테고리 없이 등록");
        int menu = InputUtil.inputInt("선택: ");

        if (menu == 2) {
            return;
        }

        if (menu != 1) {
            System.out.println("입력값이 올바르지 않아 카테고리 지정 없이 등록합니다.");
            return;
        }

        List<Category> categories = categoryRepository.findAll();
        List<Category> childCategories = new ArrayList<>();

        // 중분류 카테고리만 사용
        for (Category category : categories) {
            if (category.getParentCategoryId() != null) {
                childCategories.add(category);
            }
        }

        // 카테고리 없음
        if (childCategories.isEmpty()) {
            System.out.println("등록된 중분류 카테고리가 없어 상품만 등록되었습니다.");
            return;
        }

        // 카테고리 -> 정렬순서
        childCategories.sort(Comparator.comparingInt(Category::getSortOrder));

        /*
            ===== 중분류 카테고리 선택 =====
            1. 노트북 (C101, 정렬순서: 1)
            2. 모니터 (C102, 정렬순서: 2)
            3. 주방용품 (C201, 정렬순서: 1)
            4. 청소용품 (C202, 정렬순서: 2)
            5. 욕실용품 (C203, 정렬순서: 3)
            6. 볼펜 (D401, 정렬순서: 1)
         */
        System.out.println("\n===== 중분류 카테고리 선택 =====");
        for (int i = 0; i < childCategories.size(); i++) {
            Category category = childCategories.get(i);
            System.out.println((i + 1) + ". " + category.getCategoryName() + " (" + category.getCategoryId() + ", 정렬순서: " + category.getSortOrder() + ")");
        }

        int choice = InputUtil.inputInt("번호 선택: ");

        // 상품보다 크면 return
        if (choice < 1 || choice > childCategories.size()) {
            System.out.println("번호를 다시 확인해주세요. 상품만 등록됩니다.");
            return;
        }

        // 카테고리 순서 입력
        Category selectedCategory = childCategories.get(choice - 1);
        int sortOrder = InputUtil.inputPositiveInt("카테고리 내 상품 정렬순서 입력: ");

        List<ProductCategoryMapping> mappings = mappingRepository.findAll();

        // 같은 상품-카테고리 매핑 중복 등록 방지
        for (ProductCategoryMapping mapping : mappings) {
            if (mapping.getProductId().equals(productId) && mapping.getCategoryId().equals(selectedCategory.getCategoryId())) {
                System.out.println("이미 등록된 매핑입니다.");
                return;
            }
        }

        ProductCategoryMapping newMapping = new ProductCategoryMapping(productId, selectedCategory.getCategoryId(), sortOrder);

        mappingRepository.save(newMapping);
        System.out.println("카테고리 매핑이 등록되었습니다.");
    }

    // 상품과 매핑 같이 삭제
    private void deleteMappingsByProductId(String productId) {
        List<ProductCategoryMapping> mappings = mappingRepository.findAll();
        List<ProductCategoryMapping> remainingMappings = new ArrayList<>();

        for (ProductCategoryMapping mapping : mappings) {
            if (!mapping.getProductId().equals(productId)) {
                remainingMappings.add(mapping);
            }
        }

        mappingRepository.updateAll(remainingMappings);
    }

    /**
     *  @param productId 찾으려는 상품의 아이디
     *  @param products  검색 대상이 되는 상품들의 리스트
     *
     *  @return 찾은 Product 객체, 없으면 null 반환
     */
    private Product findProductOrPrintError(String productId, List<Product> products) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }

        System.out.println("상품 ID를 다시 확인해주세요.");
        return null;
    }

    // 상품 출력
    private void printProduct(Product product) {
        System.out.println("상품ID : " + product.getProductId());
        System.out.println("상품명 : " + product.getProductName());
        System.out.println("설명 : " + product.getProductDescription());
        System.out.println("가격 : " + product.getPrice());
        System.out.println("재고 : " + product.getStockQuantity());
        System.out.println("상태 : " + product.getProductStatus());
        System.out.println("------------------------");
    }
}