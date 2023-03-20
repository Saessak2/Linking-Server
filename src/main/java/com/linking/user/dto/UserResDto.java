package com.linking.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResDto {

    private Long userId;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;

}
