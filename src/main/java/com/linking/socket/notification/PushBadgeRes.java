package com.linking.socket.notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushBadgeRes {

    private int badge;

    public PushBadgeRes(int badge) {
        this.badge = badge;
    }
}
