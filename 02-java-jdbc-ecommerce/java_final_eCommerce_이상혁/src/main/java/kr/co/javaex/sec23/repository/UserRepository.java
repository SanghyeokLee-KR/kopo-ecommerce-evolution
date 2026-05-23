package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.User;
import kr.co.javaex.sec23.util.common.enums.user.UserRole;
import kr.co.javaex.sec23.util.common.enums.user.UserStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public List<User> findAll(Connection con) throws SQLException {
        List<User> users = new ArrayList<>();

        String sql = """
                SELECT USER_NO, USER_ID, USER_NAME, USER_PASSWORD, USER_PHONE,
                       USER_EMAIL, USER_STATUS, USER_ROLE, CREATED_AT
                FROM USERS
                ORDER BY USER_NO
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapToUser(rs));
            }
        }

        return users;
    }

    public User findByUserId(Connection con, String userId) throws SQLException {
        String sql = """
                SELECT USER_NO, USER_ID, USER_NAME, USER_PASSWORD, USER_PHONE,
                       USER_EMAIL, USER_STATUS, USER_ROLE, CREATED_AT
                FROM USERS
                WHERE USER_ID = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToUser(rs);
                }
            }
        }

        return null;
    }

    public User findByEmail(Connection con, String email) throws SQLException {
        String sql = """
                SELECT USER_NO, USER_ID, USER_NAME, USER_PASSWORD, USER_PHONE,
                       USER_EMAIL, USER_STATUS, USER_ROLE, CREATED_AT
                FROM USERS
                WHERE USER_EMAIL = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToUser(rs);
                }
            }
        }

        return null;
    }

    public void save(Connection con, User user) throws SQLException {
        String sql = """
                INSERT INTO USERS (
                    USER_NO, USER_ID, USER_NAME, USER_PASSWORD,
                    USER_PHONE, USER_EMAIL, USER_STATUS, USER_ROLE, CREATED_AT
                ) VALUES (
                    SEQ_USERS.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, SYSDATE
                )
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getUserPassword());
            pstmt.setString(4, user.getUserPhoneNumber());
            pstmt.setString(5, user.getUserEmail());
            pstmt.setString(6, user.getUserStatus().name());
            pstmt.setString(7, user.getUserRole().name());

            pstmt.executeUpdate();
        }
    }

    public void update(Connection con, User user) throws SQLException {
        String sql = """
                UPDATE USERS
                SET USER_NAME = ?,
                    USER_PASSWORD = ?,
                    USER_PHONE = ?,
                    USER_EMAIL = ?,
                    USER_STATUS = ?,
                    USER_ROLE = ?
                WHERE USER_NO = ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getUserPassword());
            pstmt.setString(3, user.getUserPhoneNumber());
            pstmt.setString(4, user.getUserEmail());
            pstmt.setString(5, user.getUserStatus().name());
            pstmt.setString(6, user.getUserRole().name());
            pstmt.setInt(7, user.getUserNo());

            pstmt.executeUpdate();
        }
    }

    public void updatePassword(Connection con, int userNo, String newPassword) throws SQLException {
        String sql = "UPDATE USERS SET USER_PASSWORD = ? WHERE USER_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userNo);
            pstmt.executeUpdate();
        }
    }

    public void updateStatus(Connection con, int userNo, UserStatus status) throws SQLException {
        String sql = "UPDATE USERS SET USER_STATUS = ? WHERE USER_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setInt(2, userNo);
            pstmt.executeUpdate();
        }
    }

    public void updateRole(Connection con, int userNo, UserRole role) throws SQLException {
        String sql = "UPDATE USERS SET USER_ROLE = ? WHERE USER_NO = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, role.name());
            pstmt.setInt(2, userNo);
            pstmt.executeUpdate();
        }
    }

    public User findByUserNo(Connection con, int userNo) throws SQLException {
        String sql = """
            SELECT USER_NO, USER_ID, USER_NAME, USER_PASSWORD, USER_PHONE,
                   USER_EMAIL, USER_STATUS, USER_ROLE, CREATED_AT
            FROM USERS
            WHERE USER_NO = ?
            """;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToUser(rs);
                }
            }
        }

        return null;
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .userNo(rs.getInt("USER_NO"))
                .userId(rs.getString("USER_ID"))
                .userName(rs.getString("USER_NAME"))
                .userPassword(rs.getString("USER_PASSWORD"))
                .userPhoneNumber(rs.getString("USER_PHONE"))
                .userEmail(rs.getString("USER_EMAIL"))
                .userStatus(UserStatus.valueOf(rs.getString("USER_STATUS")))
                .userRole(UserRole.valueOf(rs.getString("USER_ROLE")))
                .createdAt(rs.getDate("CREATED_AT"))
                .build();
    }
}