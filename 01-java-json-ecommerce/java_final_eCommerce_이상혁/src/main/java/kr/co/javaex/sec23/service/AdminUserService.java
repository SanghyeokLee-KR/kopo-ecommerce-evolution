package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.User;
import kr.co.javaex.sec23.repository.UserRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.user.UserRole;
import kr.co.javaex.sec23.util.common.enums.user.UserStatus;

import java.util.List;

/**
 *  <h3>관리자용 회원 관리 기능을 처리하는 서비스</h3>
 * - 전체 회원 조회
 * - 회원 승인
 * - 탈퇴 요청 처리
 * - 권한 변경
 */
public class AdminUserService {

    private final UserRepository userRepository = new UserRepository();

    /**
     * 전체 회원 목록을 조회
     */
    public void showAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            System.out.println("회원 정보가 없습니다.");
            return;
        }

        System.out.println("\n===== 전체 회원 목록 =====");

        for (User user : users) {
            System.out.println("회원ID : " + user.getUserId());
            System.out.println("회원명 : " + user.getUserName());
            System.out.println("전화번호 : " + user.getUserPhoneNumber());
            System.out.println("이메일 : " + user.getUserEmail());
            System.out.println("상태 : " + user.getUserStatus());
            System.out.println("권한 : " + user.getUserRole());
            System.out.println("------------------------");
        }
    }

    /**
     * 가입요청 상태의 회원을 정상 상태로 승인
     */
    public void approveUser() {
        String userId = InputUtil.inputRequiredLine("승인할 회원ID 입력: ");
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                // 탈퇴 완료 회원은 승인 대상에서 제외
                if (user.getUserStatus() == UserStatus.탈퇴완료) {
                    System.out.println("탈퇴 완료 회원은 승인할 수 없습니다.");
                    return;
                }

                user.setUserStatus(UserStatus.정상);
                userRepository.updateAll(users);
                System.out.println("회원 승인 완료");
                return;
            }
        }

        System.out.println("해당 회원이 없습니다.");
    }

    /**
     * 탈퇴요청 상태의 회원을 탈퇴완료 상태로 변경
     */
    public void processWithdraw() {
        String userId = InputUtil.inputRequiredLine("탈퇴 처리할 회원ID 입력: ");
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                // 이미 탈퇴된 회원이거나 탈퇴요청 상태가 아니면 처리 불가
                if (user.getUserStatus() == UserStatus.탈퇴완료) {
                    System.out.println("이미 탈퇴 처리된 회원입니다.");
                    return;
                }

                if (user.getUserStatus() != UserStatus.탈퇴요청) {
                    System.out.println("탈퇴 요청 상태의 회원만 처리할 수 있습니다.");
                    return;
                }

                user.setUserStatus(UserStatus.탈퇴완료);
                userRepository.updateAll(users);
                System.out.println("탈퇴 처리가 완료되었습니다.");
                return;
            }
        }

        System.out.println("해당 회원이 없습니다.");
    }

    /**
     * 회원 권한 변경
     */
    public void changeUserRole() {
        String userId = InputUtil.inputRequiredLine("권한 변경할 회원ID 입력: ");
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                // 탈퇴 완료 회원은 권한 변경 대상에서 제외
                if (user.getUserStatus() == UserStatus.탈퇴완료) {
                    System.out.println("탈퇴 완료 회원은 권한을 변경할 수 없습니다.");
                    return;
                }

                System.out.println("1. 일반회원");
                System.out.println("2. 관리자");
                int roleChoice = InputUtil.inputInt("변경할 권한 선택: ");

                switch (roleChoice) {
                    case 1:
                        user.setUserRole(UserRole.일반회원);
                        break;
                    case 2:
                        user.setUserRole(UserRole.관리자);
                        break;
                    default:
                        System.out.println("잘못된 입력입니다.");
                        return;
                }

                userRepository.updateAll(users);
                System.out.println("권한 변경 완료");
                return;
            }
        }

        System.out.println("해당 회원이 없습니다.");
    }
}