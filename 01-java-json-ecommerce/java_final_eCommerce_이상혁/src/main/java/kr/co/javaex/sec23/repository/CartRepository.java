package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.Cart;
import kr.co.javaex.sec23.util.json.JsonUtil;

import java.nio.file.Path;
import java.util.List;


/**
 * <h3>카트 레파지토리</h3>
 */
public class CartRepository {

    private final Path path = Path.of("src/data/carts.json");

    // 카테고리 json파일 전체 리스트로 가져오기
    public List<Cart> findAll() {
        return JsonUtil.readCarts(path.toString());
    }

    // 받아온 값 json파일로 저장
    public void save(Cart cart) {
        List<Cart> cartList = findAll();
        cartList.add(cart);
        JsonUtil.writeCarts(path.toString(), cartList);
    }

    public void updateAll(List<Cart> cartList) {
        JsonUtil.writeCarts(path.toString(), cartList);
    }
}