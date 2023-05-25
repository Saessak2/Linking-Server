package com.linking.page.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.page.persistence.IPageSocketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class PageWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final IPageSocketRepository pageWebSocketSessionRepositoryImpl;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        log.info("PageWebSocketHandler.afterConnectionEstablished");

        Long projectId = (Long) session.getAttributes().get("projectId");
        Long pageId = (Long) session.getAttributes().get("pageId");
        Long userId = (Long) session.getAttributes().get("userId");

        log.info("projectId = {} | pageId = {} | userId = {} | session.id = {}", projectId, pageId, userId, session.getId());

        int size = pageWebSocketSessionRepositoryImpl.save(pageId, session);
        log.info("sessions size of page {} is {}", pageId, size);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("session.getPayload = {}", message.getPayload());
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



