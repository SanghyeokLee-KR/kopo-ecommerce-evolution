package kr.co.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "tb_user")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SequenceGenerator(name = "tb_user_seq_generator", sequenceName = "seq_tb_user", allocationSize = 1)
public class User {

    // 사용자 식별번호(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_user_seq_generator")
    @Column(name = "nb_user")
    private Long nbUser;

    // 로그인 ID
    @Column(name = "id_user", nullable = false, unique = true, length = 100)
    private String idUser;

    // 사용자명
    @Column(name = "nm_user", nullable = false, length = 100)
    private String nmUser;

    // 비밀번호
    @Column(name = "nm_paswd", nullable = false, length = 256)
    private String nmPaswd;

    // 휴대전화
    @Column(name = "no_mobile", nullable = false, length = 30)
    private String noMobile;

    // 이메일
    @Column(name = "nm_email", nullable = false, unique = true, length = 100)
    private String nmEmail;

    // 상태 코드 (ST01, ST02, ST03, ST04)
    @Column(name = "st_status", nullable = false, length = 4)
    private String stStatus;

    // 사용자 구분 코드 (10: 일반사용자, 20: 관리자)
    @Column(name = "cd_user_type", nullable = false, length = 4)
    private String cdUserType;

    // 회원 정보 수정
    public void updateUserInfo(String nmUser, String noMobile, String nmEmail) {
        this.nmUser = nmUser;
        this.noMobile = noMobile;
        this.nmEmail = nmEmail;
    }

    // 비밀번호 변경
    public void changePassword(String nmPaswd) {
        this.nmPaswd = nmPaswd;
    }

    // 회원 상태 변경
    public void changeStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    // 사용자 구분 변경
    public void changeUserType(String cdUserType) {
        this.cdUserType = cdUserType;
    }
}