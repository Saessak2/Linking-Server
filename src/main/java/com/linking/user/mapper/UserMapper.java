package com.linking.user.mapper;

import com.linking.user.domain.User;
import com.linking.user.dto.UserDetailedRes;
import com.linking.user.dto.UserSignUpDefaultReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailedRes toRes(User user);
    User toUser(UserSignUpDefaultReq userSignUpDefaultReq);

}
