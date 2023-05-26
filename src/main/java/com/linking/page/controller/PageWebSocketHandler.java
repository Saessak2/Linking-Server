package com.linking.page.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.global.util.JsonMapper;
import com.linking.page.dto.TextInputMessage;
import com.linking.page.persistence.IPageSocketRepository;
import com.linking.page.service.PageEditingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PageEditingService pageEditingService;
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

        TextInputMessage textInputMessage = objectMapper.readValue(message.getPayload(), TextInputMessage.class);
        log.info("type = {}, index ={}, char = {}", textInputMessage.getInputType(), textInputMessage.getIndex(), textInputMessage.getCharacter());

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("projectId", session.getAttributes().get("projectId"));
        attributes.put("pageId", session.getAttributes().get("pageId"));
        attributes.put("userId", session.getAttributes().get("userId"));
        attributes.put("sessionId", session.getId());

        pageEditingService.inputText(attributes, textInputMessage);


        Set<WebSocketSession> sessions = pageSocketSessionRepositoryImpl.findByPageId((Long) session.getAttributes().get("pageId"));

        if (sessions == null) return;

        sessions.forEach(ws -> {
            try {
                if ((ws.getId() != session.getId()) && ws.isOpen())
                    ws.sendMessage(new TextMessage(JsonMapper.toJsonString(message.getPayload())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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



