package com.linking.global.common;

import com.linking.chatroom.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@AllArgsConstructor
public class ChattingSession {

    private Long userId;
    private ChatRoom chatRoom;
    private WebSocketSession session;

}
