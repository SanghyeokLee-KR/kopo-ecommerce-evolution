package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.Order;
import kr.co.javaex.sec23.util.common.enums.order.OrderStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    // 전체 주문 목록 조회
    public List<Order> findAll(Connection con) throws SQLException {
        List<Order> orders = new ArrayList<>();

        String sql = """
                SELECT ORDER_NO, USER_NO, ORDER_STATUS, ORDER_DATE, TOTAL_PRICE
                FROM ORDERS
                ORDER BY ORDER_NO DESC
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                orders.add(mapToOrder(rs));
            }
        }

        return orders;
    }

    // 회원번호로 주문 목록 조회
    public List<Order> findByUserNo(Connection con, int userNo) throws SQLException {
        List<Order> orders = new ArrayList<>();

        String sql = """
                SELECT ORDER_NO, USER_NO, ORDER_STATUS, ORDER_DATE, TOTAL_PRICE
                FROM ORDERS
                WHERE USER_NO = ?
                ORDER BY ORDER_NO DESC
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapToOrder(rs));
                }
            }
        }

        return orders;
    }

    // 단건 조회
    public Order findByOrderNo(Connection con, int orderNo) throws SQLException {
        String sql = """
                SELECT ORDER_NO, USER_NO, ORDER_STATUS, ORDER_DATE, TOTAL_PRICE
                FROM ORDERS
                WHERE ORDER_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, orderNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToOrder(rs);
                }
            }
        }

        return null;
    }

    // 관리자 전체 주문 목록 조회
    public List<Order> findAllWithUser(Connection con) throws SQLException {
        List<Order> orders = new ArrayList<>();

        String sql = """
                SELECT O.ORDER_NO,
                       O.USER_NO,
                       U.USER_NAME,
                       O.ORDER_STATUS,
                       O.ORDER_DATE,
                       O.TOTAL_PRICE
                FROM ORDERS O
                JOIN USERS U
                  ON O.USER_NO = U.USER_NO
                ORDER BY O.ORDER_NO DESC
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                orders.add(
                        Order.builder()
                                .orderNo(rs.getInt("ORDER_NO"))
                                .userNo(rs.getInt("USER_NO"))
                                .userName(rs.getString("USER_NAME"))
                                .orderStatus(OrderStatus.valueOf(rs.getString("ORDER_STATUS")))
                                .orderDate(rs.getDate("ORDER_DATE"))
                                .totalPrice(rs.getInt("TOTAL_PRICE"))
                                .build()
                );
            }
        }

        return orders;
    }

    // 관리자 주문 상세 기본 정보 조회
    public Order findDetailWithUserByOrderNo(Connection con, int orderNo) throws SQLException {
        String sql = """
                SELECT O.ORDER_NO,
                       O.USER_NO,
                       U.USER_NAME,
                       O.ORDER_STATUS,
                       O.ORDER_DATE,
                       O.TOTAL_PRICE
                FROM ORDERS O
                JOIN USERS U
                  ON O.USER_NO = U.USER_NO
                WHERE O.ORDER_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, orderNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Order.builder()
                            .orderNo(rs.getInt("ORDER_NO"))
                            .userNo(rs.getInt("USER_NO"))
                            .userName(rs.getString("USER_NAME"))
                            .orderStatus(OrderStatus.valueOf(rs.getString("ORDER_STATUS")))
                            .orderDate(rs.getDate("ORDER_DATE"))
                            .totalPrice(rs.getInt("TOTAL_PRICE"))
                            .build();
                }
            }
        }

        return null;
    }

    // 주문 등록
    public int save(Connection con, Order order) throws SQLException {
        String sql = """
                INSERT INTO ORDERS (
                    ORDER_NO, USER_NO, ORDER_STATUS, ORDER_DATE, TOTAL_PRICE
                ) VALUES (
                    SEQ_ORDERS.NEXTVAL, ?, ?, SYSDATE, ?
                )
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql, new String[]{"ORDER_NO"})) {
            pstmt.setInt(1, order.getUserNo());
            pstmt.setString(2, order.getOrderStatus().name());
            pstmt.setInt(3, order.getTotalPrice());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        try (PreparedStatement pstmt = con.prepareStatement("SELECT SEQ_ORDERS.CURRVAL FROM DUAL");
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new SQLException("주문번호 생성 실패");
    }

    // 주문 상태 수정
    public void updateStatus(Connection con, int orderNo, OrderStatus orderStatus) throws SQLException {
        String sql = "UPDATE ORDERS SET ORDER_STATUS = ? WHERE ORDER_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, orderStatus.name());
            pstmt.setInt(2, orderNo);
            pstmt.executeUpdate();
        }
    }

    // RS 객체 변환 메서드
    private Order mapToOrder(ResultSet rs) throws SQLException {
        return Order.builder()
                .orderNo(rs.getInt("ORDER_NO"))
                .userNo(rs.getInt("USER_NO"))
                .orderStatus(OrderStatus.valueOf(rs.getString("ORDER_STATUS")))
                .orderDate(rs.getDate("ORDER_DATE"))
                .totalPrice(rs.getInt("TOTAL_PRICE"))
                .build();
    }
}