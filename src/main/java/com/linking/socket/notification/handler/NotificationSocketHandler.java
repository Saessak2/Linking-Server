package com.linking.socket.notification.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.global.util.JsonMapper;
import com.linking.socket.notification.PushMessageReq;
import com.linking.socket.notification.PushSendEvent;
import com.linking.socket.notification.PushWebSocketSession;
import com.linking.socket.notification.persistence.NotificationSocketSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final NotificationSocketSessionRepository sessionRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Long userId = (Long) session.getAttributes().get("userId");

        log.info("[PUSH_SOCKET] userId = {}, sessionId = {}", userId, session.getId());

        int size = sessionRepository.save(userId, new PushWebSocketSession(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // todo isChecking 메시지 전송

        PushMessageReq pushMessageReq = null;
        try {
            pushMessageReq = objectMapper.readValue(message.getPayload(), PushMessageReq.class);
        } catch (JsonParseException exception) {
            log.error("PushMessageReq.class 형식에 맞지 않습니다. => ", exception.getMessage());
        }

        // todo userId 뽑아서 Set<PushWebSocketSession> 찾아
        // todo 그중에 세션 id로 일치하는 세션 찾아서 isChecking을 바꿔줘

        Long userId = (Long) session.getAttributes().get("userId");

        Set<PushWebSocketSession> sessions = sessionRepository.findByUserId(userId);
        for (PushWebSocketSession se : sessions) {
            if (se.getWebSocketSession().getId().equals(session.getId())) {
                se.setIsChecking(pushMessageReq.getIsChecking());
                break;
            }
        }
    }


    @EventListener
    public void sendEvent(PushSendEvent event) {
        log.info("send PushSendEvent Message");

        Set<PushWebSocketSession> sessions = sessionRepository.findByUserId(event.getUserId());

        try {
            if (event.getType().equals("badge")) {
                if (sessions != null && !sessions.isEmpty()) {
                    sessions.forEach(session -> {
                        if (session.getWebSocketSession().isOpen()) {
                            try {
                                session.getWebSocketSession().sendMessage(new TextMessage(JsonMapper.toJsonString(event.getData())));
                            } catch (IOException e) {
                                log.error("IOException in PushSendEvent -> {}", e.getMessage());
                            }
                        }
                    });
                    log.info("complete pushSendEvent");
                }

            } else if (event.getType().equals("push")) {
                if (sessions != null && !sessions.isEmpty()) {
                    sessions.forEach(session -> {
                        if (session.getIsChecking() && session.getWebSocketSession().isOpen()) {
                            try {
                                session.getWebSocketSession().sendMessage(new TextMessage(JsonMapper.toJsonString(event.getData())));
                            } catch (IOException e) {
                                log.error("IOException in PushSendEvent -> {}", e.getMessage());
                            }
                        }
                    });
                    log.info("complete pushSendEvent");
                }
            }
        } catch (RuntimeException e) {
            log.error("{} in PushSendEvent -> {}", e.getClass(), e.getMessage());
        }
    }
}
