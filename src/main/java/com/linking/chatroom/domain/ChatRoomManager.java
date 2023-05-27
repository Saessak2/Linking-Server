package com.linking.chatroom.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chat.dto.ResType;
import com.linking.chatroom_badge.domain.ChatRoomBadge;
import com.linking.chatroom_badge.persistence.ChatRoomBadgeRepository;
import com.linking.global.common.ChattingSession;
import com.linking.participant.domain.Participant;
import com.linking.chat.dto.ChatFocusingUserRes;
import lombok.Getter;
import lombok.Setter;
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

    public ChatRoomManager(ChatRoom chatRoom){
        this.projectId = chatRoom.getProject().getProjectId();
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

    public void sendTextMessageToSessions(ObjectMapper objectMapper, ChatRoomBadgeRepository chatRoomBadgeRepository, List<Participant> participantList, TextMessage textMessage) throws RuntimeException {
        List<ChattingSession> notFocusing = chattingSessionList.stream().filter(c -> !c.getIsFocusing() && c.getWebSocketSession().isOpen() ).collect(Collectors.toList());
        List<Participant> unregpartList = new ArrayList<>();

        List<Participant> pL = notFocusing.stream().map(ChattingSession::getParticipant).collect(Collectors.toList());
        List<ChatRoomBadge> chatRoomBadges = chatRoomBadgeRepository.findChatRoomBadgesByParticipantContaining(unregpartList);
        chatRoomBadges.addAll(chatRoomBadgeRepository.findChatRoomBadgesByParticipantContaining(pL));

        for(ChatRoomBadge cd : chatRoomBadges) {
            cd.plusCount();
            chatRoomBadgeRepository.save(cd);
        }

        for(ChattingSession cs: notFocusing) {
            try {
                int num =  chatRoomBadges.stream().findAny()
                        .filter(c -> c.getParticipant().getParticipantId().equals(cs.getParticipant().getParticipantId())).get().getUnreadCount();
                Map<String, Object> map = new HashMap<>();
                map.put("resType", ResType.badgeAlarm);
                map.put("data", num);
                cs.getWebSocketSession().sendMessage(new TextMessage(objectMapper.writeValueAsString(map)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        chattingSessionList.forEach(cs -> {
            try {
                if(cs.getWebSocketSession().isOpen() && cs.getIsFocusing())
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

    public void setChattingSessionFocusState(ChatRoomBadgeRepository chatRoomBadgeRepository, WebSocketSession session, boolean isFocusing){
        for(ChattingSession cs : chattingSessionList){
            if(cs.getWebSocketSession().getId().equals(session.getId())) {
                cs.setIsFocusing(isFocusing);
                if(isFocusing) {
                    ChatRoomBadge chatRoomBadge = chatRoomBadgeRepository.findChatRoomBadgeByParticipant(cs.getParticipant()).orElseThrow(NoSuchElementException::new);
                    chatRoomBadge.resetCnt();
                    chatRoomBadgeRepository.save(chatRoomBadge);
                }
                break;
            }
        }

    }

}
