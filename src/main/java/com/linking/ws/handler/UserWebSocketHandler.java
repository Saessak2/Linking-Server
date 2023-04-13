package com.linking.ws.handler;

import com.linking.document.dto.DocumentEvent;
import com.linking.document.service.DocumentService;
import com.linking.group.dto.GroupRes;
import com.linking.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
@RequiredArgsConstructor
public class UserWebSocketHandler extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(UserWebSocketHandler.class);

    // 연결 중인 세션 저장
    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static Map<Long, Set<WebSocketSession>> sessionsByProject = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("\n==================== afterConnectionEstablished ============================ : {}" , session );
        logger.info("\nsession.get.getUri.getQuery : {}", session.getUri().getQuery());
        Long key = 8L;

        if (!sessions.containsKey(session.getId())) {
            sessions.put(session.getId(), session);
            if (sessionsByProject.containsKey(key)) {
                sessionsByProject.get(key).add(session);
            }
            else {
                Set<WebSocketSession> hashSet = new HashSet<>();
                hashSet.add(session);
                sessionsByProject.put(key, hashSet);
            }
        }
//        logger.info("sessions ==+++> {}", sessions.get(session.getId()));
    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("\nsessionId ========> {}", session.getId());
        logger.info("\ncloseStatus ======> {}", status);
        super.afterConnectionClosed(session, status);
    }



    @EventListener
    public void sendDocuments(DocumentEvent documentEvent)  {
        logger.info("event listener");
        Set<WebSocketSession> webSocketSessions = sessionsByProject.get(documentEvent.getProjectId());
        webSocketSessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(JsonMapper.toJsonString(documentEvent.getGroupResList())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        logger.info("===================== handleTextMessage ============================");
//        logger.info("attributes : {} ", session.getAttributes());
//
//        String payload = message.getPayload();
//        if (payload.contains("DOC")) {
//            List<GroupRes> allDocuments = documentService.findAllDocuments(8L);
//            session.sendMessage(new TextMessage(JsonMapper.toJsonString(allDocuments)));
//        }
//    }

    @Scheduled(cron = "0/30 * * * * *")
    public void ping() {
//        logger.info("현재 쓰레드 : {}", Thread.currentThread().getName());
        try {
            for (WebSocketSession session : sessions.values()) {
                if (session.isOpen()) {
                    session.sendMessage(new PingMessage());
                    logger.info("session is opened");
                }
                else {
                    sessions.remove(session.getId());
                    logger.info("session is closed");
                }
            }
        } catch (IOException e) {
            logger.error("Exception while ping session");
        }
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        logger.info("receive pong from client");
    }
}
