package com.linking.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chatroom.domain.ChattingSession;
import com.linking.chatroom.repository.ChatRoomRepository;
import com.linking.chatroom.service.ChatRoomManagerService;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chat.dto.ChatReq;
import com.linking.chat.service.ChatService;
import com.linking.global.exception.BadRequestException;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.project.domain.Project;
import com.linking.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChattingWebSocketHandler extends AbstractWebSocketHandler {

    private final ObjectMapper objectMapper;

    private final ChatService chatService;
    private final ChatRoomManagerService chatRoomManagerService;
    private final ChatRoomRepository chatRoomRepository;

    private final ParticipantRepository participantRepository;

    private final String es = "-------------------------------------------------------------------------------------";

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        log.info("[ CHATTING ] [ CONNECTED SESSION {} ] {}", session.getId(), es);
        System.out.println(session);
    }

    @Override
    public void handleMessage(@Nonnull WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        if(webSocketMessage instanceof TextMessage)
            handleTextMessage(session, (TextMessage) webSocketMessage);
        else if(webSocketMessage instanceof BinaryMessage)
            handleBinaryMessage(session, (BinaryMessage) webSocketMessage);
        else
            log.error("[ CHATTING ] CAN NOT PARSE RECEIVED MESSAGE {}", es);
    }

    @Override
    protected void handleTextMessage(@Nonnull WebSocketSession session, TextMessage textMessage) throws IOException {
        ChatReq chatReq = objectMapper.readValue(textMessage.getPayload(), ChatReq.class);
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByProject(new Project(chatReq.getProjectId()))
                .orElseThrow(NoSuchElementException::new);
        log.info("[ CHATROOM {}, USER {} ] [ MESSAGE {}#{} ] RECEIVED {}", chatRoom.getChatRoomId(), chatReq.getUserId(), chatReq.getSentDatetime(), chatReq.getContent(), es);
        Participant participant = participantRepository.findByUserAndProjectId(chatReq.getUserId(), chatReq.getProjectId()).orElseThrow(NoSuchElementException::new);

        switch(chatReq.getReqType()) {
            case register:
                chatRoomManagerService.registerChattingSession(chatReq.getProjectId(), chatRoom,
                        new ChattingSession(chatReq.getProjectId(), participant, false, session));
                log.info("[ CHATROOM {}, USER {} ] REGISTERED {}", chatRoom.getChatRoomId(), chatReq.getUserId(), es);
                break;

            case open:
                chatRoomManagerService.changeChattingSessionFocusState(chatReq.getProjectId(), chatRoom, session, true);
                log.info("[ CHATROOM {}, USER {} ] OPENED {}", chatRoom.getChatRoomId(), chatReq.getUserId(), es);
                break;

            case text:
                chatRoomManagerService.publishMessage(chatReq.getProjectId(), chatRoom, chatService.saveChat(chatRoom, chatReq));
                log.info("[ CHATROOM {}, USER {} ] MESSAGE SENT {}", chatRoom.getChatRoomId(), chatReq.getUserId(), es);
                break;

            case close:
                log.info("[ CHATROOM {}, USER {} ] CLOSED {}", chatRoom.getChatRoomId(), chatReq.getUserId(), es);
                chatRoomManagerService.changeChattingSessionFocusState(chatReq.getProjectId(), chatRoom, session, false);
                break;

            case disconnect:
                log.info("[ CHATROOM {}, USER {} ] DISCONNECTED {}", chatRoom.getChatRoomId(), chatReq.getUserId(), es);
                chatRoomManagerService.disconnectSession(chatReq.getProjectId(), chatRoom, session);
                break;

            default:
                throw new BadRequestException("Request Type Mismatch");
        }
    }

}
