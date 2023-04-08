package com.linking.user.persistence;

import com.linking.user.domain.User;
import com.linking.user.dto.UserDetailedRes;
import com.linking.user.dto.UserSignUpReq;
import com.linking.user.dto.UserRes;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailedRes toDto(User user);
    List<UserDetailedRes> toDto(List<User> list);
    User toEntity(UserSignUpReq userSignUpReq);
    UserRes toUserResDto(User user);
}
