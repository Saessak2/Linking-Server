package com.linking.util;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class Message<T> {

    // 통신시간
    private LocalDateTime transactionTime;

    // 응답코드
    private String resultCode;

    // 주고 받을 데이터
    private T data;

    public static <T> Message<T> OK() {
        return (Message<T>) Message.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .build();
    }

    public static <T> Message<T> OK(T data) {
        return (Message<T>) Message.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .data(data)
                .build();
    }

    public static <T> Message<T> ERROR() {
        return (Message<T>) Message.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("ERROR")
                .build();
    }

}
