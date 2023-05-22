package com.linking.message.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chatroom.repository.ChatRoomMapper;
import com.linking.chatroom.service.ChatRoomService;
import com.linking.global.common.ChattingSession;
import com.linking.message.domain.Message;
import com.linking.message.dto.MessageReq;
import com.linking.message.dto.MessageType;
import com.linking.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageWebSocketHandler extends AbstractWebSocketHandler {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMapper chatRoomMapper;

    private final MessageService messageService;

    private final ObjectMapper objectMapper;

    private final List<ChattingSession> chattingSessions = new ArrayList<>();

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        if(webSocketMessage instanceof TextMessage)
            handleTextMessage(session, (TextMessage) webSocketMessage);
        else if(webSocketMessage instanceof BinaryMessage)
            handleBinaryMessage(session, (BinaryMessage) webSocketMessage);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws IOException {
        MessageReq messageReq = objectMapper.readValue(textMessage.getPayload(), MessageReq.class);
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomService.getChatRoomById(messageReq.getProjectId()));

        if(messageReq.getMessageType().equals(MessageType.open)) {
            log.info("[ CHATTING ] [ ROOM {} ] ENTERED", chatRoom);
            chattingSessions.add(new ChattingSession(messageReq.getUserId(), chatRoom, session));
        }
        else if(messageReq.getMessageType().equals(MessageType.text)) {
            ChattingSession cs = new ChattingSession(messageReq.getUserId(), chatRoom, session);
            log.info("[ CHATTING ] [ ROOM {} ] MESSAGE SENT", chatRoom);
            System.out.println("접혻한 세션 아이디"+session.getId());
            for(int i = 0; i < chattingSessions.size(); i++){
                System.out.println("목록 아이디"+chattingSessions.get(i).getSession().getId());

                if(session.getId().equals(chattingSessions.get(i).getSession().getId())) {
                    cs = chattingSessions.get(i);
                    break;
                }
            }
//            ChattingSession chattingSession =
//                    chattingSessions.stream()
//                            .findAny().filter(cs -> cs.getSession().getId().equals(session.getId()))
//                            .orElseThrow(NoSuchElementException::new);
            publishMessage(cs, messageReq);
        }
        else if(messageReq.getMessageType().equals(MessageType.close)) {
            log.info("[ CHATTING ] [ ROOM {} ] EXITED", chatRoom);
            ChattingSession cs = new ChattingSession(messageReq.getUserId(), chatRoom, session);;
//            ChattingSession chattingSession =
//                    chattingSessions.stream()
//                            .findAny().filter(cs -> cs.getSession().getId().equals(session.getId()))
//                            .orElseThrow(NoSuchElementException::new);
            System.out.println("접속한 세션 아이디"+session.getId());
            for(int i = 0; i < chattingSessions.size(); i++){
                System.out.println("목록의 아이디"+chattingSessions.get(i).getSession().getId());

                if(session.getId().equals(chattingSessions.get(i).getSession().getId())) {
                    cs.getSession().close();
                    chattingSessions.remove(i);
                    break;
                }
            }
        }
    }

    private void publishMessage(ChattingSession chattingSession, MessageReq messageReq){
        Set<WebSocketSession> sessions =
                chattingSessions.stream()
                        .filter(cs -> cs.getChatRoom().getChatRoomId().equals(chattingSession.getChatRoom().getChatRoomId()))
                        .map(ChattingSession::getSession).collect(Collectors.toSet());

        Message message = messageService.saveMessage(chattingSession.getChatRoom(), messageReq);
        sessions
                .forEach(session -> messageService.sendMessages(session, message));
    }

}
