package kr.co.shop.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kr.co.shop.member.entity.User;
import kr.co.shop.member.entity.UserRole;
import kr.co.shop.member.entity.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 5, max = 15, message = "아이디는 5~15자리로 입력해주세요.")
    private String userId;

    @NotBlank(message = "이름을 입력해주세요.")
    private String userName;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{5,15}$",
            message = "비밀번호는 대문자, 소문자, 숫자를 포함한 5~15자리여야 합니다."
    )
    private String userPassword;

    @NotBlank(message = "휴대전화를 입력해주세요.")
    private String userPhone;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String userEmail;

    // 회원가입 요청값을 User 엔티티로 변환
    public User toEntity(String encodedPassword) {
        return User.builder()
                .userId(userId)
                .userName(userName)
                .userPassword(encodedPassword)
                .userPhone(userPhone)
                .userEmail(userEmail)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();
    }
}