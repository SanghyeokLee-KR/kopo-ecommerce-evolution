package kr.co.shop.repository.mapper;

import kr.co.shop.dto.user.response.UserListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 사용자 조회 Mapper
 */
@Mapper
public interface UserMapper {

    // 관리자 회원 목록 조회
    List<UserListResponse> findUsers(
            @Param("stStatus") String stStatus,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // 관리자 회원 목록 총 개수
    int countUsers(
            @Param("stStatus") String stStatus
    );
}