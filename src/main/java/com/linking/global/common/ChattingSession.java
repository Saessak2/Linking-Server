package com.linking.global.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linking.chatroom.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@AllArgsConstructor
public class ChattingSession {

    private Long userId;
    private Long projectId;
    private ChatRoom chatRoom;
    private Boolean isFocusing;
    private WebSocketSession session;

    @JsonIgnore
    public void setIsFocusing(Boolean isFocusing){
        this.isFocusing = isFocusing;
    }

}
