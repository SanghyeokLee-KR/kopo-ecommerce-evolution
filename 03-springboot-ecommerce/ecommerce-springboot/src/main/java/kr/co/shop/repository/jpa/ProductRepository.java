package kr.co.shop.repository.jpa;

import kr.co.shop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품 Repository
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}