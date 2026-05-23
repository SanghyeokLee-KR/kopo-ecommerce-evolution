package kr.co.javaex.sec23.util.db;

import oracle.security.pki.OraclePKIProvider;

import java.security.Security;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String URL = getEnv("DB_URL");
    private static final String USER = getEnv("DB_USER");
    private static final String PASSWORD = getEnv("DB_PASSWORD");
    private static final String TNS_ADMIN = getEnv("TNS_ADMIN");

    static {
        System.setProperty("oracle.net.tns_admin", TNS_ADMIN);
        Security.addProvider(new OraclePKIProvider());
    }

    private DBUtil() {}

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("DB 연결 실패", e);
        }
    }

    public static void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                throw new RuntimeException("롤백 실패", e);
            }
        }
    }

    public static void close(Connection con) {
        if (con != null) {
            try {
                if (!con.getAutoCommit()) {
                    con.setAutoCommit(true);
                }
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException("커넥션 종료 실패", e);
            }
        }
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(key + " 환경변수가 설정되지 않았습니다.");
        }
        return value;
    }
}