package com.linking.message.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRes {

    private String userName;
    private String content;
    private String sentDatetime;

}
