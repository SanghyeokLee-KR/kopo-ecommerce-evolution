package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.Product;
import kr.co.javaex.sec23.util.json.JsonUtil;

import java.nio.file.Path;
import java.util.List;


/**
 * <h3>상품 레파지토리</h3>
 */
public class ProductRepository {

    private final Path path = Path.of("src/data/products.json");

    // 카테고리 json파일 전체 리스트로 가져오기
    public List<Product> findAll() {
        return JsonUtil.readProducts(path.toString());
    }

    // 아이디값에 관련된거 가져옴
    public Product findById(String productId) {
        List<Product> productList = findAll();

        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }

        return null;
    }

    // 받아온 값 json파일로 저장
    public void save(Product product) {
        List<Product> productList = findAll();
        productList.add(product);
        JsonUtil.writeProducts(path.toString(), productList);
    }

    public void updateAll(List<Product> productList) {
        JsonUtil.writeProducts(path.toString(), productList);
    }
}