package com.linking.global.auth;

import lombok.*;

import java.io.Serializable;

@Getter
public class UserCheck implements Serializable {

    private Long userId;

    public UserCheck(Long userId) {
        this.userId = userId;
    }
}
