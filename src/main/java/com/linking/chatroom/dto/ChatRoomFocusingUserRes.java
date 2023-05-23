package com.linking.chatroom.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomFocusingUserRes {

    private Long userId;
    private String userName;

}
