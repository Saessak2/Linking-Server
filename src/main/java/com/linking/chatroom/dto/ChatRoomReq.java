package com.linking.chatroom.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomReq {

    @NotNull
    private Long userId;

    @NotNull
    private Long projectId;

}
