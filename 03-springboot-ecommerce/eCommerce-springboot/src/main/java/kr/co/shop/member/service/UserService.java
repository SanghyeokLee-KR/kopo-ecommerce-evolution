package kr.co.shop.member.service;

import kr.co.shop.member.dto.UserDto;
import kr.co.shop.member.entity.User;

public interface UserService {

    User login(String email, String password);

    Long join(UserDto userDto);
}