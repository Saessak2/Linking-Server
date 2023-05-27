package com.linking.chatroom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chat.dto.ChatRes;
import com.linking.chat.dto.ResType;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chatroom.domain.ChatRoomManager;
import com.linking.global.common.ChattingSession;
import com.linking.chatroom_badge.domain.ChatRoomBadge;
import com.linking.chatroom_badge.persistence.ChatRoomBadgeRepository;
import com.linking.participant.domain.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatRoomManagerService {

    private final List<ChatRoomManager> chatRoomManagers = new ArrayList<>();
    private final ObjectMapper objectMapper;

    private final ChatRoomBadgeRepository chatRoomBadgeRepository;

    public void registerChattingSessionOnChatRoom(ChatRoom chatRoom, Participant participant, WebSocketSession session) {
        ChattingSession chattingSession = new ChattingSession(chatRoom.getProject(), participant, false, session);
        ChatRoomManager chatRoomManager = getChatRoomManager(chatRoom);
        chatRoomManager.getChattingSessionList().add(chattingSession);
        ChatRoomBadge chatRoomBadge = chatRoomBadgeRepository
                .findChatRoomBadgeByParticipant(chattingSession.getParticipant())
                .orElseThrow(NoSuchElementException::new);

        try {
            chatRoomManager.sendTextMessage(chattingSession, objectMapper, ResType.badgeAlarm, chatRoomBadge.getUnreadCount());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openChatRoom(ChatRoom chatRoom, WebSocketSession session) {
        changeChattingSessionFocusState(chatRoom, session, true);
    }

    public void publishTextMessage(ChatRoom chatRoom, ChatRes chatRes) {
        ChatRoomManager chatRoomManager =
                chatRoomManagers.stream()
                        .filter(c -> c.getProjectId().equals(chatRoom.getProject().getProjectId())).findAny()
                        .orElseThrow(NoSuchElementException::new);
        chatRoomManager.sendTextChatMessage(objectMapper, chatRoomBadgeRepository, ResType.textMessage, chatRes);
    }

    public void closeChatRoom(ChatRoom chatRoom, WebSocketSession session) {
        changeChattingSessionFocusState(chatRoom, session, false);
    }

    public void unregisterChattingSessionOnChatRoom(ChatRoom chatRoom, WebSocketSession webSocketSession){
        ChatRoomManager chatRoomManager =
                chatRoomManagers.stream()
                        .filter(c -> c.getProjectId().equals(chatRoom.getProject().getProjectId())).findAny()
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

    private ChatRoomManager getChatRoomManager(ChatRoom chatRoom){
        boolean isChatRoomManagerAvailable = false;
        ChatRoomManager chatRoomManager = new ChatRoomManager(chatRoom);

        for(ChatRoomManager c : chatRoomManagers){
            if(c.getProjectId().equals(chatRoom.getProject().getProjectId())){
                chatRoomManager = c;
                isChatRoomManagerAvailable = true;
                break;
            }
        }

        if(!isChatRoomManagerAvailable)
            chatRoomManagers.add(chatRoomManager);
        return chatRoomManager;
    }
    
    private void changeChattingSessionFocusState(ChatRoom chatRoom, WebSocketSession session, boolean isFocusing) {
        ChatRoomManager chatRoomManager = chatRoomManagers.stream()
                .filter(c -> c.getChatRoom().getChatRoomId().equals(chatRoom.getChatRoomId())).findAny()
                .orElseThrow(NoSuchElementException::new);

        chatRoomManager.setChattingSessionFocusState(chatRoomBadgeRepository, session, isFocusing);
        chatRoomManager.sendFocusingUsers(objectMapper);
    }

    private void disconnectSession(WebSocketSession session){
        for(ChatRoomManager chatRoomManager : chatRoomManagers){
            chatRoomManager.deleteChattingSession(session);
        }
    }

}
