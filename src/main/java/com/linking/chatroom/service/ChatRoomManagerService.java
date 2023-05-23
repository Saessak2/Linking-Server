package com.linking.chatroom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chatroom.domain.ChatRoomManager;
import com.linking.chatroom.domain.ChattingSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatRoomManagerService {

    private final List<ChatRoomManager> chatRoomManagers = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public void registerChattingSession(Long projectId, ChatRoom chatRoom, ChattingSession chattingSession) {
        boolean isExist = false;
        ChatRoomManager chatRoomManager = new ChatRoomManager(projectId, chatRoom);

        for(ChatRoomManager crm : chatRoomManagers){
            if(crm.getProjectId().equals(projectId)){
                chatRoomManager = crm;
                isExist = true;
                break;
            }
        }
        if(!isExist)
            chatRoomManagers.add(chatRoomManager);
//        ChatRoomManager chatRoomManager =
//                chatRoomManagers.stream().findAny().filter(m -> m.getChatRoom().equals(chatRoom))
//                        .orElse(new ChatRoomManager(chatRoom));
        chatRoomManager.getChattingSessionSet().add(chattingSession);
//        chatRoomManagers.add(chatRoomManager);
    }

    public void changeChattingSessionFocusState(Long projectId, ChatRoom chatRoom, WebSocketSession session, boolean isFocusing) throws JsonProcessingException {
        ChatRoomManager chatRoomManager = new ChatRoomManager(projectId, chatRoom);
        for(ChatRoomManager crm : chatRoomManagers){
            if(chatRoomManager.getProjectId().equals(projectId)){
                chatRoomManager = crm;
                break;
            }
        }
//        ChatRoomManager chatRoomManager =
//                chatRoomManagers.stream().findAny().filter(m -> m.getProjectId().equals(projectId))
//                        .orElseThrow(NoSuchElementException::new);
        System.out.println(session.getId());
        chatRoomManager.setChattingSessionFocusState(session, isFocusing);
        publishFocusingUserList(chatRoomManager);
    }

    public void publishMessage(Long projectId, ChatRoom chatRoom, TextMessage textMessage){
//        ChatRoomManager chatRoomManager =
//                chatRoomManagers.stream().findAny().filter(m -> m.getProjectId().equals(projectId))
//                        .orElseThrow(NoSuchElementException::new);
        ChatRoomManager chatRoomManager = new ChatRoomManager(projectId, chatRoom);
        for(ChatRoomManager crm : chatRoomManagers){
            if(chatRoomManager.getProjectId().equals(projectId)){
                chatRoomManager = crm;
                break;
            }
        }
        chatRoomManager.sendTextMessageToSessions(textMessage);
    }

    public void disconnectSession(Long projectId, ChatRoom chatRoom, WebSocketSession webSocketSession) throws IOException {
//        ChatRoomManager chatRoomManager =
//                chatRoomManagers.stream().findAny().filter(m -> m.getProjectId().equals(projectId))
//                        .orElseThrow(NoSuchElementException::new);
        ChatRoomManager chatRoomManager = new ChatRoomManager(projectId, chatRoom);
        for(ChatRoomManager crm : chatRoomManagers){
            if(chatRoomManager.getProjectId().equals(projectId)){
                chatRoomManager = crm;
                break;
            }
        }
        chatRoomManager.deleteChattingSession(webSocketSession);
        webSocketSession.close();
    }

    private void publishFocusingUserList(ChatRoomManager chatRoomManager) throws JsonProcessingException {
        chatRoomManager.sendTextMessageToSessions(new TextMessage(objectMapper.writeValueAsString(chatRoomManager.getFocusingUsers())));
    }


}
