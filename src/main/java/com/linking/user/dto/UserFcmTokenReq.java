package com.linking.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFcmTokenReq {

    @NotNull
    private Long userId;
    @NotNull
    private String fcmToken;

    @Builder
    public UserFcmTokenReq(Long userId, String fcmToken) {
        this.userId = userId;
        this.fcmToken = fcmToken;
    }
}
