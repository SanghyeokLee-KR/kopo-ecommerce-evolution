package kr.co.shop.member.service;

import kr.co.shop.member.dto.request.UserJoinRequest;

public interface UserService {

    Long join(UserJoinRequest request);
}