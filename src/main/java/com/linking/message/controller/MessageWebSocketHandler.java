package com.linking.message.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chatroom.repository.ChatRoomMapper;
import com.linking.chatroom.service.ChatRoomService;
import com.linking.global.common.ChattingSession;
import com.linking.global.common.ResponseHandler;
import com.linking.message.domain.Message;
import com.linking.message.dto.MessageReq;
import com.linking.message.dto.MessageRes;
import com.linking.message.dto.MessageType;
import com.linking.message.persistence.MessageMapper;
import com.linking.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.w3c.dom.Text;

import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageWebSocketHandler extends AbstractWebSocketHandler {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMapper chatRoomMapper;

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    private final ObjectMapper objectMapper;

    private final Set<ChattingSession> chattingSessions = new HashSet<>();

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        if(webSocketMessage instanceof TextMessage)
            handleTextMessage(session, (TextMessage) webSocketMessage);
        else if(webSocketMessage instanceof BinaryMessage)
            handleBinaryMessage(session, (BinaryMessage) webSocketMessage);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws JsonProcessingException {
        MessageReq messageReq = objectMapper.readValue(textMessage.getPayload(), MessageReq.class);
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomService.getChatRoomById(messageReq.getProjectId()));
        ChattingSession chattingSession = new ChattingSession(messageReq.getUserId(), chatRoom, session);
        log.info("[ CHATTING ] [ ROOM {} ] ENTERING", chatRoom);

        if(messageReq.getMessageType().equals(MessageType.register))
            chattingSessions.add(chattingSession);
        else if(messageReq.getMessageType().equals(MessageType.text))
            publishMessage(chattingSession, messageReq);
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
