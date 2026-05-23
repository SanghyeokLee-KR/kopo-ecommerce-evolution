package kr.co.javaex.sec23.service;

import kr.co.javaex.sec23.domain.User;
import kr.co.javaex.sec23.repository.UserRepository;
import kr.co.javaex.sec23.util.common.InputUtil;
import kr.co.javaex.sec23.util.common.enums.user.UserRole;
import kr.co.javaex.sec23.util.common.enums.user.UserStatus;

import java.util.List;

/**
 * <h3>유저 서비스</h3>
 * 사용자의 회원가입, 로그인, 로그아웃, 회원 정보관리
 */
public class UserService {

    private final UserRepository userRepository = new UserRepository();
    private User loginUser;

    /**
     * <h3>회원가입</h3> </br>
     * • 회원ID : 영문자, 숫자 사용 가능, 5 ~ 15자리(내부용, PK) </br>
     * • 회원명 </br>
     * • 비밀번호 : 영문자(대문자/소문자 1 이상 포함)+숫자(1 이상 포함), 5 ~ 15자리 </br>
     * • 휴대전화 </br>
     * • 이메일 (로그인처리, Unique) </br>
     * • 상태 : ‘가입요청’, ‘정상’ </br>
     * • 회원구분 : ‘일반회원’, ‘관리자
     */
    public void signUp() {
        System.out.println("========== 회원가입 ==========");

        System.out.println("- 회원 아이디 규칙");
        System.out.println("- 영문자, 숫자 사용 가능");
        System.out.println("- 길이 5 ~ 15자");
        String userId = InputUtil.inputLine("회원ID 입력: ");

        // 아이디 정규식
        if (!isValidUserId(userId)) {
            System.out.println("아이디를 다시 확인해주세요.");
            return;
        }

        if (userRepository.findByUserId(userId) != null) {
            System.out.println("이미 사용 중인 아이디입니다.");
            return;
        }

        String userName = InputUtil.inputLine("회원명 입력: ");

        System.out.println("\n- 비밀번호 규칙");
        System.out.println("- 대문자 1개 이상");
        System.out.println("- 소문자 1개 이상");
        System.out.println("- 숫자 1개 이상");
        System.out.println("- 길이 5 ~ 15자");
        String userPassword = InputUtil.inputLine("비밀번호 입력: ");

        // 비밀번호 정규식 확인
        if (!isValidPassword(userPassword)) {
            System.out.println("비밀번호 규칙이 맞지 않습니다..");
            return;
        }

        String phoneNumber = InputUtil.inputLine("전화번호 입력: ");
        String email = InputUtil.inputLine("이메일 입력: ");

        // 이메일 중복 확인
        if (userRepository.findByEmail(email) != null) {
            System.out.println("이미 사용 중인 이메일입니다.");
            return;
        }

        User newUser = new User(userId, userName, userPassword, phoneNumber, email, UserStatus.가입요청, UserRole.일반회원);

        userRepository.save(newUser);

        System.out.println("회원가입 완료");
    }

    /**
     * <h3>로그인</h3>
     * 이메일 및 비밀번호 로그인
     *
     * @return 로그인 성공시 true 로그인 실패시 false
     */
    public boolean login() {
        System.out.println("========== 로그인 ==========");

        String email = InputUtil.inputLine("이메일 입력: ");
        String password = InputUtil.inputLine("비밀번호 입력: ");

        // 이메일을 매개변수로 보내 레파지토리에서 반환 받는다.
        User user = userRepository.findByEmail(email);

        // 계정 null 확인
        if (user == null) {
            System.out.println("존재하지 않는 계정입니다.");
            return false;
        }

        if (!user.getUserPassword().equals(password)) {
            System.out.println("비밀번호가 일치하지 않습니다.");
            return false;
        }

        // UserStatus 정상 회원만 접근 가능
        if (user.getUserStatus() != UserStatus.정상) {
            System.out.println("정상 상태의 회원만 로그인할 수 있습니다.");
            return false;
        }

        loginUser = user;
        System.out.println(loginUser.getUserName() + "님 로그인되었습니다.");
        return true;
    }

    /**
     * <h3>로그아웃</h3>
     * 사용자 로그아웃 한다. loginUser의 참조를 멈춘다.
     */
    public void logout() {
        if (loginUser == null) {
            System.out.println("로그인된 회원이 없습니다.");
            return;
        }

        loginUser = null;
        System.out.println("로그아웃되었습니다.");
    }

    /**
     * <h3>회원 정보 출력</h3>
     */
    public void showMyInfo() {
        if (loginUser == null) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        System.out.println("========== 내 정보 ==========");
        System.out.println("회원ID : " + loginUser.getUserId());
        System.out.println("회원명 : " + loginUser.getUserName());
        System.out.println("전화번호 : " + loginUser.getUserPhoneNumber());
        System.out.println("이메일 : " + loginUser.getUserEmail());
    }

    /**
     * <h3>회원정보 수정</h3>
     * 회원명, 휴대전화, 이메일
     */
    public void updateMyInfo() {
        if (loginUser == null) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        // Json파일을 다 읽어온다.
        List<User> users = userRepository.findAll();

        String newUserName = InputUtil.inputLine("새 회원명 입력: ");
        String newPhoneNumber = InputUtil.inputLine("새 전화번호 입력: ");
        String newEmail = InputUtil.inputLine("새 이메일 입력: ");

        // 있는 이메일인지 확인한다.
        User existingUserByEmail = userRepository.findByEmail(newEmail);

        // 찾은 이메일에 id를 가져와 참조하는 로그인유저 객체에 id가 같은지 확인한다.
        if (!existingUserByEmail.getUserId().equals(loginUser.getUserId())) {
            System.out.println("이미 사용 중인 이메일입니다.");
            return;
        }

        // for문을 돌려 일치하는 아이디를 찾고 이름, 폰, 이메일을 값을 바꾼다.
        for (User user : users) {
            if (user.getUserId().equals(loginUser.getUserId())) {
                user.setUserName(newUserName);
                user.setUserPhoneNumber(newPhoneNumber);
                user.setUserEmail(newEmail);

                // 지금 수정한다.
                loginUser = user;
                break;
            }
        }

        // users 수정한 정보를 업데이트
        userRepository.updateAll(users);

        System.out.println("회원정보 수정 완료");
    }

    /**
     * 비밀번호 변경
     */
    public void changePassword() {
        if (loginUser == null) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        String currentPassword = InputUtil.inputLine("현재 비밀번호 입력: ");

        // 현재 비밀번호와 같은지
        if (!loginUser.getUserPassword().equals(currentPassword)) {
            System.out.println("현재 비밀번호가 일치하지 않습니다.");
            return;
        }

        System.out.println("\n[새 비밀번호 규칙]");
        System.out.println("- 대문자 1개 이상");
        System.out.println("- 소문자 1개 이상");
        System.out.println("- 숫자 1개 이상");
        System.out.println("- 길이 5 ~ 15자");

        String newPassword = InputUtil.inputLine("새 비밀번호 입력: ");

        // 비빌번호 정규식
        if (!isValidPassword(newPassword)) {
            System.out.println("비밀번호 형식이 올바르지 않습니다. 규칙을 확인해주세요.");
            return;
        }

        List<User> users = userRepository.findAll();

        // 로그인 회원의 비밀번호 변경
        for (User user : users) {
            if (user.getUserId().equals(loginUser.getUserId())) {
                user.setUserPassword(newPassword);
                loginUser = user;
                break;
            }
        }

        userRepository.updateAll(users);
        System.out.println("비밀번호가 변경되었습니다.");
    }

    /**
     * <h3>회원 탈퇴 요청(선택)</h3>
     * PDF에 탈퇴 요청이라 써져있어서 탈퇴 요청으로 변경 UserStatus.탈퇴요청 ENUM 열거형
     */
    public void requestWithdraw() {
        if (loginUser == null) {
            System.out.println("로그인이 필요합니다.");
            return;
        }

        // Json파일 읽어와서 users 리스트에
        List<User> users = userRepository.findAll();

        // 회원 상태를 탈퇴요청으로 변경
        for (User user : users) {
            // user의 현재 세션 id가 일치하는지
            if (user.getUserId().equals(loginUser.getUserId())) {
                user.setUserStatus(UserStatus.탈퇴요청);
                break;
            }
        }

        // 수중하기
        userRepository.updateAll(users);
        loginUser = null;

        System.out.println("탈퇴 요청이 처리되었습니다.");
    }

    // 로그인 세션 보내기
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