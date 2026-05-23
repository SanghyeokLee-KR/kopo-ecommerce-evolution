package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.User;
import kr.co.javaex.sec23.repository.UserRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.user.UserRole;
import kr.co.javaex.sec23.util.common.enums.user.UserStatus;
import kr.co.javaex.sec23.util.db.DBUtil;

import java.sql.Connection;
import java.util.List;

public class AdminUserService {

    private final UserRepository userRepository = new UserRepository();

    // 전체 회원 조회
    public void showAllUsers() {
        try (Connection con = DBUtil.getConnection()) {
            List<User> users = userRepository.findAll(con);

            if (users.isEmpty()) {
                System.out.println("회원 정보가 없습니다.");
                return;
            }

            System.out.println("\n===== 전체 회원 목록 =====");

            for (User user : users) {
                System.out.println("회원번호 : " + user.getUserNo());
                System.out.println("회원ID : " + user.getUserId());
                System.out.println("회원명 : " + user.getUserName());
                System.out.println("전화번호 : " + user.getUserPhoneNumber());
                System.out.println("이메일 : " + user.getUserEmail());
                System.out.println("상태 : " + user.getUserStatus());
                System.out.println("권한 : " + user.getUserRole());
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            System.out.println("전체 회원 조회 실패: " + e.getMessage());
            throw new RuntimeException("전체 회원 조회 실패", e);
        }
    }

    // 회원 승인
    public void approveUser() {
        String userId = InputUtil.inputRequiredLine("승인할 회원ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            User user = userRepository.findByUserId(con, userId);

            if (user == null) {
                System.out.println("해당 회원이 없습니다.");
                return;
            }

            if (user.getUserStatus() == UserStatus.탈퇴완료) {
                System.out.println("탈퇴 완료 회원은 승인할 수 없습니다.");
                return;
            }

            userRepository.updateStatus(con, user.getUserNo(), UserStatus.정상);

            con.commit();
            System.out.println("회원 승인 완료");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("회원 승인 실패: " + e.getMessage());
            throw new RuntimeException("회원 승인 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 탈퇴 처리
    public void processWithdraw() {
        String userId = InputUtil.inputRequiredLine("탈퇴 처리할 회원ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            User user = userRepository.findByUserId(con, userId);

            if (user == null) {
                System.out.println("해당 회원이 없습니다.");
                return;
            }

            if (user.getUserStatus() == UserStatus.탈퇴완료) {
                System.out.println("이미 탈퇴 처리된 회원입니다.");
                return;
            }

            if (user.getUserStatus() != UserStatus.탈퇴요청) {
                System.out.println("탈퇴 요청 상태의 회원만 처리할 수 있습니다.");
                return;
            }

            userRepository.updateStatus(con, user.getUserNo(), UserStatus.탈퇴완료);

            con.commit();
            System.out.println("탈퇴 처리가 완료되었습니다.");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("탈퇴 처리 실패: " + e.getMessage());
            throw new RuntimeException("탈퇴 처리 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }

    // 권한 변경
    public void changeUserRole() {
        String userId = InputUtil.inputRequiredLine("권한 변경할 회원ID 입력: ");

        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            User user = userRepository.findByUserId(con, userId);

            if (user == null) {
                System.out.println("해당 회원이 없습니다.");
                return;
            }

            if (user.getUserStatus() == UserStatus.탈퇴완료) {
                System.out.println("탈퇴 완료 회원은 권한을 변경할 수 없습니다.");
                return;
            }

            System.out.println("1. 일반회원");
            System.out.println("2. 관리자");
            int roleChoice = InputUtil.inputInt("변경할 권한 선택: ");

            UserRole newRole;
            switch (roleChoice) {
                case 1:
                    newRole = UserRole.일반회원;
                    break;
                case 2:
                    newRole = UserRole.관리자;
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    return;
            }

            userRepository.updateRole(con, user.getUserNo(), newRole);

            con.commit();
            System.out.println("권한 변경 완료");

        } catch (Exception e) {
            DBUtil.rollback(con);
            System.out.println("권한 변경 실패: " + e.getMessage());
            throw new RuntimeException("권한 변경 실패", e);
        } finally {
            DBUtil.close(con);
        }
    }
}
