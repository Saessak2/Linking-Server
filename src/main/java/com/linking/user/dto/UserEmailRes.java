package com.linking.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailRes {

    private Boolean containingEmailExists;
    private List<UserDetailedRes> userList;

}
