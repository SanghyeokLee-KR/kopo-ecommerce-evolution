package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {

    // 전체 장바구니 목록 조회
    public List<Cart> findAll(Connection con) throws SQLException {
        List<Cart> carts = new ArrayList<>();

        String sql = """
                SELECT CART_NO, USER_NO, PRODUCT_NO, QUANTITY
                FROM CARTS
                ORDER BY CART_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                carts.add(mapToCart(rs));
            }
        }

        return carts;
    }

    // 회원번호로 장바구니 목록 조회
    public List<Cart> findByUserNo(Connection con, int userNo) throws SQLException {
        List<Cart> carts = new ArrayList<>();

        String sql = """
                SELECT CART_NO, USER_NO, PRODUCT_NO, QUANTITY
                FROM CARTS
                WHERE USER_NO = ?
                ORDER BY CART_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    carts.add(mapToCart(rs));
                }
            }
        }

        return carts;
    }

    // 내 장바구니 조회
    public List<Cart> findWithProductByUserNo(Connection con, int userNo) throws SQLException {
        List<Cart> carts = new ArrayList<>();

        String sql = """
            SELECT C.CART_NO,
                   C.USER_NO,
                   C.PRODUCT_NO,
                   C.QUANTITY,
                   P.PRODUCT_ID,
                   P.PRODUCT_NAME,
                   P.PRICE
            FROM CARTS C
            JOIN PRODUCTS P
              ON C.PRODUCT_NO = P.PRODUCT_NO
            WHERE C.USER_NO = ?
            ORDER BY C.CART_NO
        """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    carts.add(
                            Cart.builder()
                                    .cartNo(rs.getInt("CART_NO"))
                                    .userNo(rs.getInt("USER_NO"))
                                    .productNo(rs.getInt("PRODUCT_NO"))
                                    .quantity(rs.getInt("QUANTITY"))
                                    .productId(rs.getString("PRODUCT_ID"))
                                    .productName(rs.getString("PRODUCT_NAME"))
                                    .price(rs.getInt("PRICE"))
                                    .build()
                    );
                }
            }
        }

        return carts;
    }

    // 회원 + 상품 조회
    public Cart findByUserNoAndProductNo(Connection con, int userNo, int productNo) throws SQLException {
        String sql = """
                SELECT CART_NO, USER_NO, PRODUCT_NO, QUANTITY
                FROM CARTS
                WHERE USER_NO = ? AND PRODUCT_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userNo);
            pstmt.setInt(2, productNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToCart(rs);
                }
            }
        }

        return null;
    }

    // 저장
    public void save(Connection con, Cart cart) throws SQLException {
        String sql = """
                INSERT INTO CARTS (
                    CART_NO, USER_NO, PRODUCT_NO, QUANTITY
                ) VALUES (
                    SEQ_CARTS.NEXTVAL, ?, ?, ?
                )
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, cart.getUserNo());
            pstmt.setInt(2, cart.getProductNo());
            pstmt.setInt(3, cart.getQuantity());

            pstmt.executeUpdate();
        }
    }

    // 수량 수정
    public void updateQuantity(Connection con, int cartNo, int quantity) throws SQLException {
        String sql = "UPDATE CARTS SET QUANTITY = ? WHERE CART_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, cartNo);
            pstmt.executeUpdate();
        }
    }

    // 삭제
    public void deleteByCartNo(Connection con, int cartNo) throws SQLException {
        String sql = "DELETE FROM CARTS WHERE CART_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, cartNo);
            pstmt.executeUpdate();
        }
    }

    // 전체 삭제
    public void deleteByUserNo(Connection con, int userNo) throws SQLException {
        String sql = "DELETE FROM CARTS WHERE USER_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userNo);
            pstmt.executeUpdate();
        }
    }

    private Cart mapToCart(ResultSet rs) throws SQLException {
        return Cart.builder()
                .cartNo(rs.getInt("CART_NO"))
                .userNo(rs.getInt("USER_NO"))
                .productNo(rs.getInt("PRODUCT_NO"))
                .quantity(rs.getInt("QUANTITY"))
                .build();
    }
}