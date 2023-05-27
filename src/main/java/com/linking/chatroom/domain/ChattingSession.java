package com.linking.chatroom.domain;

import com.linking.participant.domain.Participant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@AllArgsConstructor
public class ChattingSession {

    private Long projectId;
    private Participant participant;
    private Boolean isFocusing;
    private WebSocketSession webSocketSession;

    public void setIsFocusing(Boolean isFocusing){
        this.isFocusing = isFocusing;
    }

}
