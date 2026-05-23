package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.ProductCategoryMapping;
import kr.co.javaex.sec23.util.json.JsonUtil;

import java.nio.file.Path;
import java.util.List;

/**
 * <h3>상품 카테고리 레파지토리</h3>
 */
public class ProductCategoryMappingRepository {

    private final Path path = Path.of("src/data/product-category-mapping.json");

    // 카테고리 json파일 전체 리스트로 가져오기
    public List<ProductCategoryMapping> findAll() {
        return JsonUtil.readProductCategoryMappings(path.toString());
    }

    // 받아온 값 json파일로 저장
    public void save(ProductCategoryMapping mapping) {
        List<ProductCategoryMapping> list = findAll();
        list.add(mapping);
        JsonUtil.writeProductCategoryMappings(path.toString(), list);
    }

    // 받아온 값 json파일로 저장
    public void updateAll(List<ProductCategoryMapping> list) {
        JsonUtil.writeProductCategoryMappings(path.toString(), list);
    }
}