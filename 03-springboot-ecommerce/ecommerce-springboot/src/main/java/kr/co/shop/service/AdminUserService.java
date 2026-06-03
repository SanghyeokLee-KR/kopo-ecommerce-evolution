package kr.co.shop.service;

import kr.co.shop.dto.user.response.UserListResponse;
import org.springframework.data.domain.Page;

/**
 * 관리자 회원 관리 서비스
 */
public interface AdminUserService {

    /**
     * 회원 목록 조회
     */
    Page<UserListResponse> findUsers(String stStatus, int page);

    /**
     * 회원 상태 변경
     */
    void changeUserStatus(Long nbUser, String stStatus);

    /**
     * 회원 권한 변경
     */
    void changeUserType(Long nbUser, String cdUserType);
}