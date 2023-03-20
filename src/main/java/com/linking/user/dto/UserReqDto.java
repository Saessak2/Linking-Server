package com.linking.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserReqDto {

    @NotNull
    private String lastName;

    @NotNull
    private String firstName;

    @NotNull
    @Email
    private String email;

    private String phoneNumber;

}
