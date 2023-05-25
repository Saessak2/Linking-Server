package com.linking.chatroom.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chatroom_badge.domain.ChatRoomBadge;
import com.linking.chatroom_badge.persistence.ChatRoomBadgeRepository;
import com.linking.participant.domain.Participant;
import com.linking.user.domain.User;
import com.linking.chat.dto.ChatFocusingUserRes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class ChatRoomManager {

    private final Long projectId;
    private final ChatRoom chatRoom;
    private final List<ChattingSession> chattingSessionList;

    public ChatRoomManager(Long projectId, ChatRoom chatRoom){
        this.projectId = projectId;
        this.chatRoom = chatRoom;
        chattingSessionList = new ArrayList<>();
    }

    public void sendFocusingUsers(TextMessage textMessage) throws RuntimeException {
        chattingSessionList.forEach(cs -> {
            try {
                if(cs.getWebSocketSession().isOpen())
                    cs.getWebSocketSession().sendMessage(textMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendTextMessageToSessions(ChatRoomBadgeRepository chatRoomBadgeRepository, List<Participant> participantList, TextMessage textMessage) throws RuntimeException {
        List<ChattingSession> notFocusing = chattingSessionList.stream().filter(c -> !c.getIsFocusing() && c.getWebSocketSession().isOpen() ).collect(Collectors.toList());
        List<Participant> unregpartList = new ArrayList<>();
        List<Participant> userList = chattingSessionList.stream().map(m -> m.getParticipant()).collect(Collectors.toList());

        List<Participant> pL = notFocusing.stream().map(p->p.getParticipant()).collect(Collectors.toList());;
        List<ChatRoomBadge> chatRoomBadges = chatRoomBadgeRepository.findChatRoomBadgesByParticipantContaining(unregpartList);
        chatRoomBadges.addAll(chatRoomBadgeRepository.findChatRoomBadgesByParticipantContaining(pL));

        for(ChatRoomBadge cd : chatRoomBadges)
            cd.plusCount();

        notFocusing.forEach(cs -> {
            try {
                cs.getWebSocketSession().sendMessage(new TextMessage(String.valueOf(
                        chatRoomBadges.stream().findAny()
                                .filter(c -> c.getParticipant().equals(cs.getParticipant())).get().getUnreadCount()))
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        chattingSessionList.forEach(cs -> {
            try {
                if(cs.getWebSocketSession().isOpen())
                    cs.getWebSocketSession().sendMessage(textMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void deleteChattingSession(WebSocketSession webSocketSession) {
        for(ChattingSession cs : chattingSessionList)
            if(cs.getWebSocketSession().equals(webSocketSession)) {
                chattingSessionList.remove(cs);
                break;
            }
    }

    // 중복 제거
    public List<ChatFocusingUserRes> getFocusingUsers(){
        List<ChattingSession> userList =  chattingSessionList.stream()
                .filter(ChattingSession::getIsFocusing).collect(Collectors.toList());
        List<Participant> userSet = userList.stream().map(m -> m.getParticipant()).collect(Collectors.toList());
        List<ChatFocusingUserRes> usres = new ArrayList<>();
        for(Participant participant : userSet){
            usres.add(new ChatFocusingUserRes(participant.getUser().getUserId(), participant.getUserName()));
        }
        return usres;
    }

    public void setChattingSessionFocusState(WebSocketSession session, boolean isFocusing){
        for(ChattingSession cs : chattingSessionList){
            if(cs.getWebSocketSession().getId().equals(session.getId())) {
                cs.setIsFocusing(isFocusing);
                break;
            }
        }
    }

}
