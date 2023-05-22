package com.linking.chatroom.dto;

import javax.validation.constraints.NotNull;

public class ChatRoomReq {

    @NotNull
    private Long userId;

    @NotNull
    private Long projectId;

}
