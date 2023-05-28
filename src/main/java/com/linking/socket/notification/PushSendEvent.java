package com.linking.socket.notification;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushSendEvent<T> {

    private String type;
    private Long userId;
    private T data;

    @Builder
    public PushSendEvent(String type, Long userId, T data) {
        this.type = type;
        this.userId = userId;
        this.data = data;
    }
}
