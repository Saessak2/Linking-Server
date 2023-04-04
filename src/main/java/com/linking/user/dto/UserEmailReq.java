package com.linking.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailReq {

    @NotNull
    private Long projectId;

    @NotNull
    private String partOfEmail;

}
