// AdminUserServiceImpl.java
package kr.co.shop.service;

import kr.co.shop.common.exception.BusinessException;
import kr.co.shop.common.exception.ErrorCode;
import kr.co.shop.domain.User;
import kr.co.shop.dto.user.response.UserListResponse;
import kr.co.shop.repository.jpa.UserRepository;
import kr.co.shop.repository.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserListResponse> findUsers(String stStatus, int page) {
        int pageNum = Math.max(page - 1, 0);
        int size = 6;
        int offset = pageNum * size;

        // stStatus가 null이면 전체 조회, 값이 있으면 해당 상태만 조회
        List<UserListResponse> users = userMapper.findUsers(stStatus, offset, size);
        int total = userMapper.countUsers(stStatus);

        return new PageImpl<>(users, PageRequest.of(pageNum, size), total);
    }

    @Override
    @Transactional
    public void changeUserStatus(Long nbUser, String stStatus) {
        User user = userRepository.findById(nbUser).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String beforeStatus = user.getStStatus();
        user.changeStatus(stStatus);

        log.info("회원 상태 변경 완료 - nbUser={}, {} → {}", nbUser, beforeStatus, stStatus);
    }

    @Override
    @Transactional
    public void changeUserType(Long nbUser, String cdUserType) {
        User user = userRepository.findById(nbUser).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String beforeType = user.getCdUserType();
        user.changeUserType(cdUserType);

        // 권한 변경은 보안 관련 중요 작업이므로 warn 레벨로 기록
        log.warn("회원 권한 변경 완료 - nbUser={}, {} → {}", nbUser, beforeType, cdUserType);
    }
}