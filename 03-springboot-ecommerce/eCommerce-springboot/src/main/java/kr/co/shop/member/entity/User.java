package kr.co.shop.member.entity;

import jakarta.persistence.*;
import kr.co.shop.common.entitiry.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원 정보를 저장하는 엔티티
@Getter
@Entity
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    // 회원번호 (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeqGenerator")
    @SequenceGenerator(
            name = "userSeqGenerator",
            sequenceName = "SEQ_USERS",
            allocationSize = 1
    )
    @Column(name = "USER_NO")
    private Long userNo;

    // 회원 아이디
    @Column(name = "USER_ID", nullable = false, unique = true, length = 15)
    private String userId;

    // 회원 이름
    @Column(name = "USER_NAME", nullable = false, length = 50)
    private String userName;

    // 비밀번호
    @Column(name = "USER_PASSWORD", nullable = false, length = 100)
    private String userPassword;

    // 휴대전화
    @Column(name = "USER_PHONE", nullable = false, length = 20)
    private String userPhone;

    // 이메일 (로그인 아이디로 사용)
    @Column(name = "USER_EMAIL", nullable = false, unique = true, length = 100)
    private String userEmail;

    // 회원 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "USER_STATUS", nullable = false, length = 20)
    private UserStatus userStatus;

    // 회원 권한
    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE", nullable = false, length = 20)
    private UserRole userRole;

    @Builder
    public User(String userId,
                String userName,
                String userPassword,
                String userPhone,
                String userEmail,
                UserStatus userStatus,
                UserRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    // 회원 정보 수정
    public void updateProfile(String userName, String userPhone) {
        this.userName = userName;
        this.userPhone = userPhone;
    }

    // 비밀번호 변경
    public void changePassword(String userPassword) {
        this.userPassword = userPassword;
    }

    // 회원 탈퇴 처리
    public void withdraw() {
        this.userStatus = UserStatus.WITHDRAWN;
    }
}