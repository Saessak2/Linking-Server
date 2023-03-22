package com.linking.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailedRes {

    private Long userId;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    private String password;

}
