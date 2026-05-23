package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRepository {

    // 주문번호로 주문상품 목록 조회
    public List<OrderItem> findByOrderNo(Connection con, int orderNo) throws SQLException {
        List<OrderItem> items = new ArrayList<>();

        String sql = """
                SELECT ORDER_ITEM_NO, ORDER_NO, PRODUCT_NO, ORDER_PRICE, QUANTITY, ITEM_TOTAL_PRICE
                FROM ORDER_ITEMS
                WHERE ORDER_NO = ?
                ORDER BY ORDER_ITEM_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, orderNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapToOrderItem(rs));
                }
            }
        }

        return items;
    }

    // 관리자 주문 상세 상품 목록 조회
    public List<OrderItem> findWithProductByOrderNo(Connection con, int orderNo) throws SQLException {
        List<OrderItem> items = new ArrayList<>();

        String sql = """
                SELECT OI.ORDER_ITEM_NO,
                       OI.ORDER_NO,
                       OI.PRODUCT_NO,
                       P.PRODUCT_NAME,
                       OI.ORDER_PRICE,
                       OI.QUANTITY,
                       OI.ITEM_TOTAL_PRICE
                FROM ORDER_ITEMS OI
                JOIN PRODUCTS P
                  ON OI.PRODUCT_NO = P.PRODUCT_NO
                WHERE OI.ORDER_NO = ?
                ORDER BY OI.ORDER_ITEM_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, orderNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(
                            OrderItem.builder()
                                    .orderItemNo(rs.getInt("ORDER_ITEM_NO"))
                                    .orderNo(rs.getInt("ORDER_NO"))
                                    .productNo(rs.getInt("PRODUCT_NO"))
                                    .productName(rs.getString("PRODUCT_NAME"))
                                    .orderPrice(rs.getInt("ORDER_PRICE"))
                                    .quantity(rs.getInt("QUANTITY"))
                                    .itemTotalPrice(rs.getInt("ITEM_TOTAL_PRICE"))
                                    .build()
                    );
                }
            }
        }

        return items;
    }

    // 주문상품 저장
    public void save(Connection con, OrderItem orderItem) throws SQLException {
        String sql = """
                INSERT INTO ORDER_ITEMS (
                    ORDER_ITEM_NO, ORDER_NO, PRODUCT_NO, ORDER_PRICE, QUANTITY, ITEM_TOTAL_PRICE
                ) VALUES (
                    SEQ_ORDER_ITEMS.NEXTVAL, ?, ?, ?, ?, ?
                )
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, orderItem.getOrderNo());
            pstmt.setInt(2, orderItem.getProductNo());
            pstmt.setInt(3, orderItem.getOrderPrice());
            pstmt.setInt(4, orderItem.getQuantity());
            pstmt.setInt(5, orderItem.getItemTotalPrice());

            pstmt.executeUpdate();
        }
    }

    private OrderItem mapToOrderItem(ResultSet rs) throws SQLException {
        return OrderItem.builder()
                .orderItemNo(rs.getInt("ORDER_ITEM_NO"))
                .orderNo(rs.getInt("ORDER_NO"))
                .productNo(rs.getInt("PRODUCT_NO"))
                .orderPrice(rs.getInt("ORDER_PRICE"))
                .quantity(rs.getInt("QUANTITY"))
                .itemTotalPrice(rs.getInt("ITEM_TOTAL_PRICE"))
                .build();
    }
}