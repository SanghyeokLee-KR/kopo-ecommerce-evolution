package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.Order;
import kr.co.javaex.sec23.util.json.JsonUtil;

import java.nio.file.Path;
import java.util.List;


/**
 * <h3>주문 레파지토리</h3>
 */
public class OrderRepository {

    private final Path path = Path.of("src/data/orders.json");

    // 카테고리 json파일 전체 리스트로 가져오기
    public List<Order> findAll() {
        return JsonUtil.readOrders(path.toString());
    }

    // 받아온 값 json파일로 저장
    public void updateAll(List<Order> orderList) {
        JsonUtil.writeOrders(path.toString(), orderList);
    }
}