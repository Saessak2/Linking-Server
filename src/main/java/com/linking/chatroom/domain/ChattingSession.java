package com.linking.chatroom.domain;

import com.linking.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@AllArgsConstructor
public class ChattingSession {

    private User user;
    private Long projectId;
    private Boolean isFocusing;
    private WebSocketSession webSocketSession;

    public void setIsFocusing(Boolean isFocusing){
        this.isFocusing = isFocusing;
    }

}
