package kr.co.shop.service;

import kr.co.shop.dto.product.request.ProductRequest;
import kr.co.shop.dto.product.response.ProductDetailResponse;
import kr.co.shop.dto.product.response.ProductListResponse;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 상품 서비스
 */
public interface ProductService {

    /**
     * 전체 상품 목록 조회
     */
    List<ProductListResponse> findAll();

    /**
     * 관리자 상품 목록 조회
     */
    Page<ProductListResponse> findAdminProducts(int page);

    /**
     * 사용자 상품 목록 조회
     */
    Page<ProductListResponse> findDisplayProducts(Long nbCategory, String keyword, int page);

    /**
     * 상품 상세 조회
     */
    ProductDetailResponse findById(Long nbProduct);

    /**
     * 상품 등록
     */
    void create(ProductRequest request);

    /**
     * 상품 수정
     */
    void update(Long nbProduct, ProductRequest request);

    /**
     * 상품 삭제
     */
    void delete(Long nbProduct);

    /**
     * 인덱스 페이지 최신순 사진
     */
    List<ProductListResponse> findLatestProducts(int limit);
}