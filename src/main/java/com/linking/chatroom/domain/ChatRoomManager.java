package com.linking.chatroom.domain;

import com.linking.user.domain.User;
import com.linking.chat.dto.ChatFocusingUserRes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomManager {

    private Long projectId;
    private ChatRoom chatRoom;
    private List<ChattingSession> chattingSessionSet;

    public ChatRoomManager(Long projectId, ChatRoom chatRoom){
        this.projectId = projectId;
        this.chatRoom = chatRoom;
        chattingSessionSet = new ArrayList<>();
    }

    public void sendTextMessageToSessions(TextMessage textMessage) throws RuntimeException {
        chattingSessionSet.forEach(cs -> {
            try {
                if(cs.getWebSocketSession().isOpen())
                    cs.getWebSocketSession().sendMessage(textMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void deleteChattingSession(WebSocketSession webSocketSession) {
        for(ChattingSession cs : chattingSessionSet)
            if(cs.getWebSocketSession().equals(webSocketSession)) {
                chattingSessionSet.remove(cs);
                break;
            }
    }

    // 중복 제거
    public List<ChatFocusingUserRes> getFocusingUsers(){
        List<ChattingSession> userList =  chattingSessionSet.stream()
                .filter(ChattingSession::getIsFocusing).collect(Collectors.toList());
        List<User> userSet = userList.stream().map(m -> m.getUser()).collect(Collectors.toList());
        Set<User> set = new HashSet<>(userSet);
        List<ChatFocusingUserRes> usres = new ArrayList<>();
        for(User user : set){
            usres.add(new ChatFocusingUserRes(user.getUserId(), user.getFullName()));
        }
        return usres;
    }

    public void setChattingSessionFocusState(WebSocketSession session, boolean isFocusing){
        for(ChattingSession cs : chattingSessionSet){
            if(cs.getWebSocketSession().getId().equals(session.getId())) {
                cs.setIsFocusing(isFocusing);
                break;
            }
        }
    }

}
