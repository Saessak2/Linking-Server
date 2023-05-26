package com.linking.chatroom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chat.dto.ChatRes;
import com.linking.chat_join.persistence.ChatJoinRepository;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chatroom.domain.ChatRoomManager;
import com.linking.chatroom.domain.ChattingSession;
import com.linking.chatroom_badge.domain.ChatRoomBadge;
import com.linking.chatroom_badge.persistence.ChatRoomBadgeRepository;
import com.linking.participant.domain.Participant;
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

    private final ChatJoinRepository chatJoinRepository;
    private final ChatRoomBadgeRepository chatRoomBadgeRepository;

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
        chatRoomManager.getChattingSessionList().add(chattingSession);

        ChatRoomBadge chatRoomBadge = chatRoomBadgeRepository.findChatRoomBadgeByParticipant(chattingSession.getParticipant()).orElseThrow(NoSuchElementException::new);
        Map<String, Object> map = new HashMap<>();
        map.put("resType", "badgeAlarm");
        map.put("data", chatRoomBadge.getUnreadCount());

        try {
            if(chattingSession.getWebSocketSession().isOpen())
                chattingSession.getWebSocketSession().sendMessage(new TextMessage(objectMapper.writeValueAsString(map)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeChattingSessionFocusState(Long projectId, ChatRoom chatRoom, WebSocketSession session, boolean isFocusing) throws JsonProcessingException {
        ChatRoomManager chatRoomManager = new ChatRoomManager(projectId, chatRoom);
        for(ChatRoomManager crm : chatRoomManagers){
            if(chatRoomManager.getProjectId().equals(projectId)){
                chatRoomManager = crm;
                break;
            }
        }
        System.out.println(session.getId());
        chatRoomManager.setChattingSessionFocusState(objectMapper, chatRoomBadgeRepository, session, isFocusing);
        publishFocusingUserList(chatRoomManager);
    }

    public void publishMessage(Long projectId, ChatRoom chatRoom, ChatRes chatRes) throws JsonProcessingException {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("resType", "textMessage");
        resMap.put("data", chatRes);
        ChatRoomManager chatRoomManager = new ChatRoomManager(projectId, chatRoom);
        for(ChatRoomManager crm : chatRoomManagers){
            if(chatRoomManager.getProjectId().equals(projectId)){
                chatRoomManager = crm;
                break;
            }
        }
        List<Participant> partList = chatJoinRepository.findByChatroom(chatRoom);
        chatRoomManager.sendTextMessageToSessions(objectMapper, chatRoomBadgeRepository, partList, new TextMessage(objectMapper.writeValueAsString(resMap)));
    }


    public void disconnectSession(Long projectId, ChatRoom chatRoom, WebSocketSession webSocketSession) throws IOException {
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
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("resType", "userList");
        resMap.put("data", chatRoomManager.getFocusingUsers());
        chatRoomManager.sendFocusingUsers(new TextMessage(objectMapper.writeValueAsString(resMap)));
    }

}
