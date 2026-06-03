package kr.co.shop.repository.mapper;

import kr.co.shop.dto.product.response.ProductListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 상품 조회 Mapper
 */
@Mapper
public interface ProductMapper {

    // 관리자 상품 목록 전체 조회
    List<ProductListResponse> findAllProducts();

    // 관리자 상품 목록 페이징 조회
    List<ProductListResponse> findAdminProducts(
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // 관리자 상품 목록 총 개수
    int countAdminProducts();

    // 사용자 상품 목록 조회
    List<ProductListResponse> findDisplayProducts(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // 사용자 상품 목록 총 개수
    int countDisplayProducts(
            @Param("keyword") String keyword
    );

    // 카테고리별 상품 목록 조회
    List<ProductListResponse> findProductsByCategory(
            @Param("nbCategory") Long nbCategory,
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // 카테고리별 상품 목록 총 개수
    int countProductsByCategory(
            @Param("nbCategory") Long nbCategory,
            @Param("keyword") String keyword
    );

    // 인덱스 페이지 최신순 3개
    List<ProductListResponse> findLatestProducts(int limit);
}