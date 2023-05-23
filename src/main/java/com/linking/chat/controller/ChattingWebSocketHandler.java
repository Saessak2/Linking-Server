package com.linking.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chatroom.domain.ChattingSession;
import com.linking.chatroom.service.ChatRoomManagerService;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chatroom.repository.ChatRoomMapper;
import com.linking.chatroom.service.ChatRoomService;
import com.linking.chat.dto.ChatReq;
import com.linking.chat.service.ChatService;
import com.linking.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Nonnull;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChattingWebSocketHandler extends AbstractWebSocketHandler {

    private final ObjectMapper objectMapper;

    private final ChatService chatService;

    private final ChatRoomManagerService chatRoomManagerService;

    private final ChatRoomService chatRoomService;
    private final ChatRoomMapper chatRoomMapper;


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
                chatRoomManagerService.registerChattingSession(chatReq.getProjectId(), chatRoom,
                        new ChattingSession(chatReq.getUserId(), chatReq.getProjectId(), false, session));
                log.info("[ CHATROOM {} ] [ SESSION {} ] REGISTERED", chatRoom.getChatRoomId(), session.getId());
                break;

            case open:
                chatRoomManagerService.changeChattingSessionFocusState(chatReq.getProjectId(), chatRoom, session, true);
                break;

            case text:
                chatRoomManagerService.publishMessage(chatReq.getProjectId(), chatRoom,
                        new TextMessage(objectMapper.writeValueAsString(chatService.saveChat(chatRoom, chatReq))));
                log.info("[ CHATROOM {} ] [ SESSION {} ] MESSAGE SENT", chatRoom.getChatRoomId(), session.getId());
                break;

            case close:
                chatRoomManagerService.changeChattingSessionFocusState(chatReq.getProjectId(), chatRoom, session, false);
                break;

            case disconnect:
                log.info("[ CHATROOM {} SESSION {} ] DISCONNECT ", chatRoom.getChatRoomId(), session.getId());
                chatRoomManagerService.disconnectSession(chatReq.getProjectId(), chatRoom, session);
                break;

            default:
                throw new BadRequestException("Request Type Mismatch");
        }
    }

}
