package com.linking.socket.page.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.global.util.JsonMapper;
import com.linking.socket.page.persistence.IPageSocketRepository;
import com.linking.socket.page.service.PageWebSocketService;
import com.linking.socket.page.TextInputMessage;
import com.linking.socket.page.TextSendEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class PageWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final PageWebSocketService pageWebSocketService;
    private final IPageSocketRepository pageSocketSessionRepositoryImpl;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Long projectId = (Long) session.getAttributes().get("projectId");
        Long pageId = (Long) session.getAttributes().get("pageId");
        Long userId = (Long) session.getAttributes().get("userId");

        log.info("projectId = {} | pageId = {} | userId = {} | session.id = {}", projectId, pageId, userId, session.getId());

        int size = pageSocketSessionRepositoryImpl.save(pageId, session);
        log.info("sessions size of page {} is {}", pageId, size);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        TextInputMessage textInputMessage = null;
        try {
            textInputMessage = objectMapper.readValue(message.getPayload(), TextInputMessage.class);

        } catch (JsonParseException exception) {
            log.error("TextInputMessage.class 형식에 맞지 않습니다. => {}", exception.getMessage());
        }
//        log.info("type = {}, index ={}, char = {}", textInputMessage.getInputType(), textInputMessage.getIndex(), textInputMessage.getCharacter());

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("projectId", session.getAttributes().get("projectId"));
        attributes.put("pageId", session.getAttributes().get("pageId"));
        attributes.put("userId", session.getAttributes().get("userId"));
        attributes.put("sessionId", session.getId());

        pageWebSocketService.inputText(attributes, textInputMessage);
    }

    @EventListener
    public void sendEvent(TextSendEvent event) {
        log.info("send textOutput Message");

        try {
            Set<WebSocketSession> sessions = pageSocketSessionRepositoryImpl.findByPageId(event.getPageId());
            if (sessions != null && !sessions.isEmpty()) {

                sessions.forEach(session -> {
                    if (session.getId() != event.getSessionId()) {
                        if (session.isOpen()) {
                            try {
                                session.sendMessage(new TextMessage(JsonMapper.toJsonString(event.getTextOutputMessage())));
                            } catch (IOException e) {
                                log.error("IOException in TextSendEvent -> {}", e.getMessage());
                            }
                        }
                    }
                });
                log.info("complete sendPageEvent");
            }
        } catch (RuntimeException e) {
            log.error("{} in TextSendEvent -> {}", e.getClass(), e.getMessage());
        }
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }

//    @Scheduled(cron = "0/30 * * * * *")
//    public void ping() {
//        try {
//            for (WebSocketSession session : sessions.values()) {
//                if (session.isOpen()) {
//                    session.sendMessage(new PingMessage());
////                    logger.info("session is opened");
//                } else {
//                    close(session);
//                }
//            }
//        } catch (IOException e) {
//            log.error("Exception while ping session");
//        }
//    }
}



