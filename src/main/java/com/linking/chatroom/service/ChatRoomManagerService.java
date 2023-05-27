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

    public void openChatRoom(ChatRoom chatRoom, WebSocketSession session) throws JsonProcessingException {
        changeChattingSessionFocusState(chatRoom, session, true);
    }

    public void publishTextMessage(ChatRoom chatRoom, ChatRes chatRes) throws JsonProcessingException {
        ChatRoomManager chatRoomManager =
                chatRoomManagers.stream()
                        .findAny().filter(c -> c.getProjectId().equals(chatRoom.getProject().getProjectId()))
                        .orElseThrow(NoSuchElementException::new);
        List<Participant> participantList = chatJoinRepository.findByChatroom(chatRoom);

        chatRoomManager.sendTextMessageToSessions(objectMapper, chatRoomBadgeRepository, new TextMessage(objectMapper.writeValueAsString(getResponseMap(ResType.textMessage, chatRes))));
    }

    public void closeChatRoom(ChatRoom chatRoom, WebSocketSession session) throws JsonProcessingException {
        changeChattingSessionFocusState(chatRoom, session, false);
    }

    public void unregisterChattingSessionOnChatRoom(ChatRoom chatRoom, WebSocketSession webSocketSession){
        ChatRoomManager chatRoomManager =
                chatRoomManagers.stream()
                        .findAny().filter(c -> c.getProjectId().equals(chatRoom.getProject().getProjectId()))
                        .orElseThrow(NoSuchElementException::new);
        chatRoomManager.deleteChattingSession(webSocketSession);
    }

    public void disconnectSession(ChatRoom chatRoom, WebSocketSession session) throws IOException {
        if(chatRoom == null)
            disconnectSession(session);
        else {
            unregisterChattingSessionOnChatRoom(chatRoom, session);
            session.close();
        }
    }

    private void changeChattingSessionFocusState(ChatRoom chatRoom, WebSocketSession session, boolean isFocusing) throws JsonProcessingException {
        ChatRoomManager chatRoomManager =
                chatRoomManagers.stream()
                        .findAny().filter(c -> c.getProjectId().equals(chatRoom.getProject().getProjectId()))
                        .orElseThrow(NoSuchElementException::new);
        chatRoomManager.setChattingSessionFocusState(chatRoomBadgeRepository, session, isFocusing);
        publishFocusingUserList(chatRoomManager);
    }

    private void publishFocusingUserList(ChatRoomManager chatRoomManager) throws JsonProcessingException {
        chatRoomManager.sendFocusingUsers(new TextMessage(objectMapper.writeValueAsString(getResponseMap(ResType.userList, chatRoomManager.getFocusingUsers()))));
    }

    private void disconnectSession(WebSocketSession session){
        for(ChatRoomManager chatRoomManager : chatRoomManagers){
            chatRoomManager.deleteChattingSession(session);
        }
    }

    private Map<String, Object> getResponseMap(ResType resType, Object object){
        Map<String, Object> map = new HashMap<>();
        map.put("resType", resType);
        map.put("data", object);
        return map;
    }

}
