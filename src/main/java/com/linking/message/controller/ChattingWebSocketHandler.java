package com.linking.message.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chatroom.repository.ChatRoomMapper;
import com.linking.chatroom.service.ChatRoomService;
import com.linking.message.dto.MessageReq;
import com.linking.message.persistence.MessageMapper;
import com.linking.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChattingWebSocketHandler extends AbstractWebSocketHandler {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMapper chatRoomMapper;

    private final MessageService messageService;

    private final ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String payload = textMessage.getPayload();
        log.info("{}", payload);

        MessageReq messageReq = objectMapper.readValue(payload, MessageReq.class);
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomService.getChatRoomById(messageReq.getProjectId()));

        if(!messageReq.getIsSendReq())
            chatRoom.handleEntering(session);
        else
            chatRoom.handleMessage(messageService, messageReq);
    }

//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message, String payloadType) throws Exception {
//        Object payload = message.getPayload();
//
//        log.info("{}", payload);
//        MessagePostReq messageReq = objectMapper.readValue(payload, MessagePostReq.class);
//
//        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomService.getChatRoomById(messageReq.getProjectId()));
//        chatRoom.handleEntering(session);
//
//        if(message instanceof TextMessage)
//            handleTextMessage(session, (TextMessage) message);
//        else if(message instanceof BinaryMessage)
//            handleBinaryMessage(session, (BinaryMessage) message);
//        else if(message instanceof PongMessage)
//            handlePongMessage(session, (PongMessage) message);
//        else if(payload instanceof MessagePostReq)
//            handleNormalMessage(session, (MessagePostReq) payload);
//        else {
//            throw new IllegalStateException("Unexpected WebSocket message type: " + message);
//        }
//    }
//
//    private void handleNormalMessage(WebSocketSession session, MessagePostReq messagePostReq){
//
//    }

}
