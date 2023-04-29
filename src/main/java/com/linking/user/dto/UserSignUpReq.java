package com.linking.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpReq {

    @NotNull
    private String lastName;

    @NotNull
    private String firstName;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

}
