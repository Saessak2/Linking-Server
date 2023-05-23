package com.linking.chatroom.domain;

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
    private Boolean isFocusing;
    private WebSocketSession webSocketSession;

    public void setIsFocusing(Boolean isFocusing){
        this.isFocusing = isFocusing;
    }

}
