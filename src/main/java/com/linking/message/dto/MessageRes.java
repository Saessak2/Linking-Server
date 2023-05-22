package com.linking.message.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRes {

    private Long messageId;
    private Long userId;
    private Long projectId;
    private String content;
    private String sentDatetime;

}
