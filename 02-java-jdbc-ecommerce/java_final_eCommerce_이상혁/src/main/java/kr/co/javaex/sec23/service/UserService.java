package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.User;
import kr.co.javaex.sec23.repository.UserRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.user.UserRole;
import kr.co.javaex.sec23.util.common.enums.user.UserStatus;
import kr.co.javaex.sec23.util.db.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class UserService {

    private final UserRepository userRepository = new UserRepository();
    private User loginUser;

    // 회원가입
    public void signUp() {
        System.out.println("========== 회원가입 ==========");

        System.out.println("아이디 영문 대소문자와 숫자로만 구성된 5~15자리");
        String userId = InputUtil.inputLine("회원ID 입력: ");
        if (!isValidUserId(userId)) {
            System.out.println("아이디 형식이 올바르지 않습니다.");
            return;
        }

        String userName = InputUtil.inputLine("회원명 입력: ");

        System.out.println("비밀번호는 영문 대문자, 소문자, 숫자를 모두 포함한 5~15자리");
        String userPassword = InputUtil.inputLine("비밀번호 입력: ");
        if (!isValidPassword(userPassword)) {
            System.out.println("비밀번호 규칙이 맞지 않습니다.");
            return;
        }

        String phoneNumber = InputUtil.inputLine("전화번호 입력: ");
        String email = InputUtil.inputLine("이메일 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            if (userRepository.findByUserId(con, userId) != null) {
                System.out.println("이미 사용 중인 아이디입니다.");
                return;
            }

            if (userRepository.findByEmail(con, email) != null) {
                System.out.println("이미 사용 중인 이메일입니다.");
                return;
            }

            User newUser = User.builder()
                    .userId(userId)
                    .userName(userName)
                    .userPassword(userPassword)
                    .userPhoneNumber(phoneNumber)
                    .userEmail(email)
                    .userStatus(UserStatus.가입요청)
                    .userRole(UserRole.일반회원)
                    .build();

            userRepository.save(con, newUser);

            con.commit();
            System.out.println("회원가입 완료");
        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("회원가입 실패: " + e.getMessage());
            throw new RuntimeException("회원가입 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 로그인
    public boolean login() {
        System.out.println("========== 로그인 ==========");
        String email = InputUtil.inputLine("이메일 입력: ");
        String password = InputUtil.inputLine("비밀번호 입력: ");

        try (Connection con = DBUtil.getConnection()) {
            User user = userRepository.findByEmail(con, email);

            if (user == null || !user.getUserPassword().equals(password)) {
                System.out.println("아이디 또는 비밀번호가 틀렸습니다.");
                return false;
            }

            if (user.getUserStatus() != UserStatus.정상) {
                System.out.println("로그인이 불가능한 상태의 회원입니다.");
                return false;
            }

            loginUser = user;
            System.out.println(loginUser.getUserName() + "님 로그인되었습니다.");
            return true;
        } catch (SQLException e) {
            System.out.println("로그인 처리 중 오류: " + e.getMessage());
            throw new RuntimeException("로그인 처리 중 오류", e);
        }
    }

    // 로그아웃
    public void logout() {
        if (loginUser == null) {
            System.out.println("로그인된 회원이 없습니다.");
            return;
        }
        loginUser = null;
        System.out.println("로그아웃되었습니다.");
    }

    // 내 회원정보 조회
    public void showMyInfo() {
        if (loginUser == null) {
            System.out.println("로그인이 필요합니다.");
            return;
        }
        System.out.println("========== 내 정보 ==========");
        System.out.println("회원번호 : " + loginUser.getUserNo());
        System.out.println("회원ID : " + loginUser.getUserId());
        System.out.println("회원명 : " + loginUser.getUserName());
        System.out.println("전화번호 : " + loginUser.getUserPhoneNumber());
        System.out.println("이메일 : " + loginUser.getUserEmail());
        System.out.println("상태 : " + loginUser.getUserStatus());
        System.out.println("권한 : " + loginUser.getUserRole());
    }

    // 회원정보 수정
    public void updateMyInfo() {
        if (loginUser == null) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        String newUserName = InputUtil.inputLine("새 회원명 입력: ");
        String newPhoneNumber = InputUtil.inputLine("새 전화번호 입력: ");
        String newEmail = InputUtil.inputLine("새 이메일 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            User existing = userRepository.findByEmail(con, newEmail);
            if (existing != null && existing.getUserNo() != loginUser.getUserNo()) {
                System.out.println("이미 사용 중인 이메일입니다.");
                return;
            }

            loginUser.setUserName(newUserName);
            loginUser.setUserPhoneNumber(newPhoneNumber);
            loginUser.setUserEmail(newEmail);

            userRepository.update(con, loginUser);
            con.commit();
            System.out.println("정보 수정 완료");
        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("수정 실패: " + e.getMessage());
            throw new RuntimeException("회원정보 수정 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 비밀번호 변경
    public void changePassword() {
        if (loginUser == null) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        String currentPw = InputUtil.inputLine("현재 비밀번호: ");
        if (!loginUser.getUserPassword().equals(currentPw)) {
            System.out.println("비밀번호가 일치하지 않습니다.");
            return;
        }

        String newPw = InputUtil.inputLine("새 비밀번호: ");
        if (!isValidPassword(newPw)) {
            System.out.println("비밀번호 규칙이 맞지 않습니다.");
            return;
        }

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            userRepository.updatePassword(con, loginUser.getUserNo(), newPw);
            con.commit();

            loginUser.setUserPassword(newPw);
            System.out.println("비밀번호가 변경되었습니다.");
        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("비밀번호 변경 실패: " + e.getMessage());
            throw new RuntimeException("비밀번호 변경 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 회원 탈퇴 요청
    public void requestWithdraw() {
        if (loginUser == null) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            userRepository.updateStatus(con, loginUser.getUserNo(), UserStatus.탈퇴요청);
            con.commit();

            loginUser = null;
            System.out.println("탈퇴 요청 처리 완료");
        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("탈퇴 처리 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("탈퇴 요청 처리 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    public User getLoginUser() {
        return loginUser;
    }

    // 아이디 정규식
    private boolean isValidUserId(String userId) {
        return userId.matches("^[a-zA-Z0-9]{5,15}$");
    }

    // 비밀번호 정규식
    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{5,15}$");
    }
}
