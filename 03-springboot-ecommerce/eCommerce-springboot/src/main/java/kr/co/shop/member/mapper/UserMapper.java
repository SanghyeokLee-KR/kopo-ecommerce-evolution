package kr.co.shop.member.mapper;

import kr.co.shop.member.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByEmail(String email);
}