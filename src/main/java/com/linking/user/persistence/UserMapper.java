package com.linking.user.persistence;

import com.linking.user.domain.User;
import com.linking.user.dto.UserDetailedRes;
import com.linking.user.dto.UserSignUpReq;
import com.linking.user.dto.UserTempRes;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailedRes toDto(User user);
    List<UserDetailedRes> toDto(List<User> list);
    User toEntity(UserSignUpReq userSignUpReq);

    default UserTempRes toDtoTemp(User user) {
        UserTempRes.UserTempResBuilder builder = UserTempRes.builder();
        builder
                .userId(user.getUserId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .email(user.getEmail());
        return builder.build();
    }

}
