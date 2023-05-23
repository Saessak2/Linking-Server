package com.linking.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chatroom.dto.ChatRoomFocusingUserRes;
import com.linking.chatroom.repository.ChatRoomMapper;
import com.linking.chatroom.service.ChatRoomService;
import com.linking.global.common.ChattingSession;
import com.linking.chat.domain.Chat;
import com.linking.chat.dto.ChatReq;
import com.linking.chat.service.ChatService;
import com.linking.global.common.LabeledEmitter;
import com.linking.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChattingWebSocketHandler extends AbstractWebSocketHandler {

    private final ObjectMapper objectMapper;

    private final ChatService chatService;

    private final ChatRoomService chatRoomService;
    private final ChatRoomMapper chatRoomMapper;

    private final List<ChattingSession> chattingSessions = new ArrayList<>();

    @Override
    public void handleMessage(@Nonnull WebSocketSession session, @Nonnull WebSocketMessage<?> webSocketMessage) throws Exception {
        if(webSocketMessage instanceof TextMessage)
            handleTextMessage(session, (TextMessage) webSocketMessage);
        else if(webSocketMessage instanceof BinaryMessage)
            handleBinaryMessage(session, (BinaryMessage) webSocketMessage);
    }

    @Override
    protected void handleTextMessage(@Nonnull WebSocketSession session, TextMessage textMessage) throws IOException {
        ChatReq chatReq = objectMapper.readValue(textMessage.getPayload(), ChatReq.class);
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomService.getChatRoomById(chatReq.getProjectId()));

        switch(chatReq.getReqType()) {
            case register:
                chattingSessions.add(new ChattingSession(chatReq.getUserId(), chatReq.getProjectId(), chatRoom, chatReq.getIsFocusing(), session));
                log.info("[ CHATROOM {} ] [ SESSION {} ] REGISTERED", chatRoom.getChatRoomId(), session.getId());
                break;

            case open:
                for (ChattingSession chattingSession : chattingSessions) {
                    if (session.getId().equals(chattingSession.getSession().getId())) {
                        chattingSession.setIsFocusing(true);
                        sendFocusingUserList(session, chatReq);
                        alertFocusingUserListUpdated(true, chattingSession, chatReq);
                        break;
                    }
                }

                break;

            case text:
                for (ChattingSession chattingSession : chattingSessions) {
                    if (session.getId().equals(chattingSession.getSession().getId())) {
                        publishMessage(chattingSession, chatReq);
                        break;
                    }
                }
                log.info("[ CHATROOM {} ] [ SESSION {} ] MESSAGE SENT", chatRoom.getChatRoomId(), session.getId());
                break;

            case close:
                for (ChattingSession chattingSession : chattingSessions) {
                    if (session.getId().equals(chattingSession.getSession().getId())) {
                        chattingSession.setIsFocusing(false);
                        alertFocusingUserListUpdated(false, chattingSession, chatReq);
                        break;
                    }
                }
                break;

            case disconnect:
                log.info("[ CHATROOM {} SESSION {} ] DISCONNECT ", chatRoom.getChatRoomId(), session.getId());
                for(int i = 0; i < chattingSessions.size(); i++) {
                    if(session.getId().equals(chattingSessions.get(i).getSession().getId())) {
                        chattingSessions.get(i).getSession().close();
                        chattingSessions.remove(i);
                        break;
                    }
                }
                break;

            default:
                throw new BadRequestException("Request Type Mismatch");
        }
    }

    private void publishMessage(ChattingSession chattingSession, ChatReq chatReq){
        Set<WebSocketSession> sessions =
                chattingSessions.stream()
                        .filter(cs -> cs.getChatRoom().getChatRoomId().equals(chattingSession.getChatRoom().getChatRoomId()))
                        .map(ChattingSession::getSession).collect(Collectors.toSet());

        Chat chat = chatService.saveChat(chattingSession.getChatRoom(), chatReq);
        sessions
                .forEach(session -> chatService.sendChat(session, chat));
    }

    private void sendFocusingUserList(WebSocketSession webSocketSession, ChatReq chatReq){
        List<Long> userIdList = new ArrayList<>();
        try{
            for(ChattingSession cs : chattingSessions){
                if(cs.getIsFocusing() && cs.getProjectId().equals(chatReq.getProjectId()))
                    userIdList.add(cs.getUserId());
            }
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(userIdList)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void alertFocusingUserListUpdated(boolean isNew, ChattingSession chattingSession, ChatReq chatReq){
            if (isNew) {
                Set<WebSocketSession> sessions =
                        chattingSessions.stream()
                                .filter(cs -> cs.getChatRoom().getChatRoomId().equals(chattingSession.getChatRoom().getChatRoomId()))
                                .map(ChattingSession::getSession).collect(Collectors.toSet());

                    sessions
                            .forEach(s -> {
                                try {
                                    s.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatReq.getUserId())));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

            }
    }

}
