package com.linking.chatroom.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomRes {

    private Long chatRoomId;
    private Long projectId;

}
