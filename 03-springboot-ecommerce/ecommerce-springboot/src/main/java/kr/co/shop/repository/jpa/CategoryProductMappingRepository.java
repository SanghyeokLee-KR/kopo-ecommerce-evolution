package kr.co.shop.repository.jpa;

import kr.co.shop.domain.CategoryProductMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 카테고리 상품 매핑 Repository
 */
public interface CategoryProductMappingRepository extends JpaRepository<CategoryProductMapping, Long> {

    // 상품 번호로 매핑 조회
    Optional<CategoryProductMapping> findByProduct_NbProduct(Long nbProduct);

    // 상품 번호로 매핑 삭제
    void deleteByProduct_NbProduct(Long nbProduct);
}