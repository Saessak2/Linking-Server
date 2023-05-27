package com.linking.chatroom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chat.dto.ChatRes;
import com.linking.chat.dto.ResType;
import com.linking.chat_join.persistence.ChatJoinRepository;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chatroom.domain.ChatRoomManager;
import com.linking.global.common.ChattingSession;
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

    public void registerChattingSessionOnChatRoom(ChatRoom chatRoom, Participant participant, WebSocketSession session) {
        ChattingSession chattingSession = new ChattingSession(chatRoom.getProject(), participant, false, session);
        boolean isChatRoomManagerExist = false;
        ChatRoomManager chatRoomManager = new ChatRoomManager(chatRoom);

        for(ChatRoomManager crm : chatRoomManagers){
            if(crm.getProjectId().equals(chatRoom.getProject().getProjectId())){
                chatRoomManager = crm;
                isChatRoomManagerExist = true;
                break;
            }
        }
        if(!isChatRoomManagerExist)
            chatRoomManagers.add(chatRoomManager);
        chatRoomManager.getChattingSessionList().add(chattingSession);

        ChatRoomBadge chatRoomBadge = chatRoomBadgeRepository.findChatRoomBadgeByParticipant(chattingSession.getParticipant()).orElseThrow(NoSuchElementException::new);
        Map<String, Object> map = new HashMap<>();
        map.put("resType", ResType.badgeAlarm);
        map.put("data", chatRoomBadge.getUnreadCount());

        try {
            if(chattingSession.getWebSocketSession().isOpen())
                chattingSession.getWebSocketSession().sendMessage(new TextMessage(objectMapper.writeValueAsString(map)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openChatRoom(Long projectId, ChatRoom chatRoom, WebSocketSession session, boolean isFocusing) throws JsonProcessingException {
        changeChattingSessionFocusState(projectId, chatRoom, session, isFocusing);
    }

    public void publishTextMessage(Long projectId, ChatRoom chatRoom, ChatRes chatRes) throws JsonProcessingException {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("resType", ResType.textMessage);
        resMap.put("data", chatRes);
        ChatRoomManager chatRoomManager = new ChatRoomManager(chatRoom);
        for(ChatRoomManager crm : chatRoomManagers){
            if(chatRoomManager.getProjectId().equals(projectId)){
                chatRoomManager = crm;
                break;
            }
        }
        List<Participant> partList = chatJoinRepository.findByChatroom(chatRoom);
        chatRoomManager.sendTextMessageToSessions(objectMapper, chatRoomBadgeRepository, partList, new TextMessage(objectMapper.writeValueAsString(resMap)));
    }

    public void closeChatRoom(Long projectId, ChatRoom chatRoom, WebSocketSession session, boolean isFocusing) throws JsonProcessingException {
        changeChattingSessionFocusState(projectId, chatRoom, session, isFocusing);
    }
    public void unregisterChattingSessionOnChatRoom(Long projectId, ChatRoom chatRoom, WebSocketSession webSocketSession){
        ChatRoomManager chatRoomManager = new ChatRoomManager(chatRoom);
        for(ChatRoomManager crm : chatRoomManagers){
            if(chatRoomManager.getProjectId().equals(projectId)){
                chatRoomManager = crm;
                break;
            }
        }
        chatRoomManager.deleteChattingSession(webSocketSession);
    }

    public void disconnectSession(Long projectId, ChatRoom chatRoom, WebSocketSession webSocketSession) throws IOException {
        unregisterChattingSessionOnChatRoom(projectId, chatRoom, webSocketSession);
        webSocketSession.close();
    }

    private void publishFocusingUserList(ChatRoomManager chatRoomManager) throws JsonProcessingException {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("resType", ResType.userList);
        resMap.put("data", chatRoomManager.getFocusingUsers());
        chatRoomManager.sendFocusingUsers(new TextMessage(objectMapper.writeValueAsString(resMap)));
    }



    public void changeChattingSessionFocusState(Long projectId, ChatRoom chatRoom, WebSocketSession session, boolean isFocusing) throws JsonProcessingException {
        ChatRoomManager chatRoomManager = new ChatRoomManager(chatRoom);
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

}
