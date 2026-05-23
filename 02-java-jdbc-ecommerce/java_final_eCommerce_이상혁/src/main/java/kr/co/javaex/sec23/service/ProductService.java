package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.repository.ProductCategoryMappingRepository;
import kr.co.javaex.sec23.repository.ProductRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.ProductStatus.ProductStatus;
import kr.co.javaex.sec23.util.db.DBUtil;

import java.sql.Connection;
import java.util.List;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepository();
    private final ProductCategoryMappingRepository mappingRepository = new ProductCategoryMappingRepository();

    // 전체 상품 목록 조회
    public void showAllProducts() {
        try (Connection con = DBUtil.getConnection()) {
            List<Product> products = productRepository.findAll(con);

            if (products.isEmpty()) {
                System.out.println("등록된 상품이 없습니다.");
                return;
            }

            System.out.println("\n===== 상품 목록 =====");

            for (Product product : products) {
                printProduct(product);
            }

        } catch (Exception e) {
            System.out.println("상품 목록 조회 실패: " + e.getMessage());
            throw new RuntimeException("상품 목록 조회 실패", e);
        }
    }

    // 상품 상세 정보 조회
    public void showProductDetail() {
        System.out.println("\n===== 상품 상세 조회 =====");

        String productId = InputUtil.inputRequiredLine("조회할 상품 ID 입력: ");

        try (Connection con = DBUtil.getConnection()) {
            Product product = productRepository.findByProductId(con, productId);

            if (product == null) {
                System.out.println("상품 ID를 다시 확인해주세요.");
                return;
            }

            printProduct(product);

        } catch (Exception e) {
            System.out.println("상품 상세 조회 실패: " + e.getMessage());
            throw new RuntimeException("상품 상세 조회 실패", e);
        }
    }

    // 신규 전자제품 등록
    public void addProduct() {
        System.out.println("\n===== 상품 등록 =====");

        String productId = InputUtil.inputRequiredLine("상품 ID 입력: ");
        String productName = InputUtil.inputRequiredLine("상품명 입력: ");
        String productDescription = InputUtil.inputRequiredLine("상세 설명 입력: ");
        int price = InputUtil.inputNonNegativeInt("가격 입력: ");
        int stockQuantity = InputUtil.inputNonNegativeInt("재고 수량 입력: ");

        ProductStatus productStatus = (stockQuantity == 0) ? ProductStatus.품절 : ProductStatus.정상;

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            if (productRepository.findByProductId(con, productId) != null) {
                System.out.println("이미 존재하는 상품 ID입니다.");
                return;
            }

            Product newProduct = Product.builder()
                    .productId(productId)
                    .productName(productName)
                    .productDescription(productDescription)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .productStatus(productStatus)
                    .build();

            productRepository.save(con, newProduct);

            con.commit();
            System.out.println("상품이 등록되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("상품 등록 실패: " + e.getMessage());
            throw new RuntimeException("상품 등록 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 상품 정보 수정
    public void updateProduct() {
        System.out.println("\n===== 상품 수정 =====");

        String productId = InputUtil.inputRequiredLine("수정할 상품 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Product targetProduct = productRepository.findByProductId(con, productId);

            if (targetProduct == null) {
                System.out.println("상품 ID를 다시 확인해주세요.");
                return;
            }

            String newProductName = InputUtil.inputRequiredLine("새 상품명 입력: ");
            String newProductDescription = InputUtil.inputRequiredLine("새 상세 설명 입력: ");
            int newPrice = InputUtil.inputNonNegativeInt("새 가격 입력: ");

            targetProduct.setProductName(newProductName);
            targetProduct.setProductDescription(newProductDescription);
            targetProduct.setPrice(newPrice);

            productRepository.update(con, targetProduct);

            con.commit();
            System.out.println("상품 정보가 수정되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("상품 수정 실패: " + e.getMessage());
            throw new RuntimeException("상품 수정 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 상품 삭제
    public void deleteProduct() {
        System.out.println("\n===== 상품 삭제 =====");

        String productId = InputUtil.inputRequiredLine("삭제할 상품 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Product targetProduct = productRepository.findByProductId(con, productId);

            if (targetProduct == null) {
                System.out.println("상품 ID를 다시 확인해주세요.");
                return;
            }

            productRepository.deleteByProductNo(con, targetProduct.getProductNo());
            mappingRepository.deleteByProductNo(con, targetProduct.getProductNo());

            con.commit();
            System.out.println("상품이 삭제되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("상품 삭제 실패: " + e.getMessage());
            throw new RuntimeException("상품 삭제 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 판매 중지 처리
    public void stopSellingProduct() {
        System.out.println("\n===== 판매중지 처리 =====");

        String productId = InputUtil.inputRequiredLine("상품 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Product targetProduct = productRepository.findByProductId(con, productId);

            if (targetProduct == null) {
                System.out.println("상품 ID를 다시 확인해주세요.");
                return;
            }

            productRepository.updateStatus(con, targetProduct.getProductNo(), ProductStatus.판매중지);

            con.commit();
            System.out.println("판매중지 처리가 완료되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("판매중지 처리 실패: " + e.getMessage());
            throw new RuntimeException("판매중지 처리 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 재고 수량 변경
    public void updateStock() {
        System.out.println("\n===== 재고 수정 =====");

        String productId = InputUtil.inputRequiredLine("상품 ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            Product targetProduct = productRepository.findByProductId(con, productId);

            if (targetProduct == null) {
                System.out.println("상품 ID를 다시 확인해주세요.");
                return;
            }

            int newStockQuantity = InputUtil.inputNonNegativeInt("새 재고 수량 입력: ");

            ProductStatus newStatus = targetProduct.getProductStatus();

            if (newStockQuantity == 0) {
                newStatus = ProductStatus.품절;
            } else if (targetProduct.getProductStatus() == ProductStatus.품절) {
                newStatus = ProductStatus.정상;
            }

            productRepository.updateStock(con, targetProduct.getProductNo(), newStockQuantity, newStatus);

            con.commit();
            System.out.println("재고가 수정되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("재고 수정 실패: " + e.getMessage());
            throw new RuntimeException("재고 수정 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    private void printProduct(Product product) {
        System.out.println("상품번호 : " + product.getProductNo());
        System.out.println("상품ID : " + product.getProductId());
        System.out.println("상품명 : " + product.getProductName());
        System.out.println("설명 : " + product.getProductDescription());
        System.out.println("가격 : " + product.getPrice());
        System.out.println("재고 : " + product.getStockQuantity());
        System.out.println("상태 : " + product.getProductStatus());
        System.out.println("------------------------");
    }
}
