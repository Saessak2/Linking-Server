package com.linking.ws.handler;

import com.linking.ws.event.DocumentEvent;
import com.linking.util.JsonMapper;
import com.linking.ws.message.WsMessage;
import com.linking.ws.service.WsDocumentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
@RequiredArgsConstructor
public class DocumentWebSocketHandler extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    // 연결 중인 세션 저장
    // key -> projectId
    private static Map<Long, Set<WebSocketSession>> sessionsByProject = new ConcurrentHashMap<>();

    private final WsDocumentService wsService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        logger.info("@@ [DOC] connect session.id = {} @@ projectId = {}" , session, session.getAttributes().get("projectId") );

        Long projectIdKey = (Long) session.getAttributes().get("projectId");
        Long userId = (Long) session.getAttributes().get("userId");

        // 즉시 전체 문서 리스트 전송
        try {
            session.sendMessage(new TextMessage(JsonMapper.toJsonString(wsService.getAllDocuments(projectIdKey, userId))));
        } catch (IOException e) {
            logger.error("JsonMapper.toJsonString IOException");
        }

        if (!sessionsByProject.containsKey(projectIdKey)) {
            Set<WebSocketSession> hashSet = Collections.synchronizedSet(new HashSet<>());
            hashSet.add(session);
            sessionsByProject.put(projectIdKey, hashSet);
        } else {
            if (!sessionsByProject.get(projectIdKey).contains(session))
                sessionsByProject.get(projectIdKey).add(session);
        }
        logger.info("@@ [DOC] sessionsByProject -> projectId = {}, size = {}", projectIdKey, sessionsByProject.get(projectIdKey).size());
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("@@ [DOC] -> session.id = [{}] closed ... and status is = {}", session.getId(), status);
        this.close(session);
    }

//    @Scheduled(cron = "0/30 * * * * *")
//    public void ping() {
////        logger.info("현재 쓰레드 : {}", Thread.currentThread().getName());
//        try {
//            for (WebSocketSession session : sessions.values()) {
//                if (session.isOpen()) {
//                    session.sendMessage(new PingMessage());
////                    logger.info("session is opened");
//                }
//                else {
//                    close(session);
//                }
//            }
//        } catch (IOException e) {
//            logger.error("Exception while ping session");
//        }
//    }

//    @Override
//    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
////        logger.info("receive pong from session.id = {}", session.getId());
//
//    }

    private void close(WebSocketSession session) throws IOException {
        Long projectId = (Long) session.getAttributes().get("projectId");
        session.close();
        Set<WebSocketSession> webSocketSessions = sessionsByProject.get(projectId);
        if (webSocketSessions != null && session != null)
            webSocketSessions.remove(session);
        logger.info("@@ [DOC]a projectId = {} session size = {}", projectId, webSocketSessions.size());
    }


    @EventListener
    public void sendGroups(com.linking.group.dto.DocumentEvent documentEvent) {
        logger.info("event listener");
//        logger.info("currentThread =====+++> {}", Thread.currentThread().getName());

        Set<WebSocketSession> webSocketSessions = sessionsByProject.get(documentEvent.getProjectId());
        for (WebSocketSession session : webSocketSessions) {
            try {
                if (session.isOpen())
                    session.sendMessage(new TextMessage(JsonMapper.toJsonString(documentEvent.getGroupResList())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @EventListener
    public void sendEvent(DocumentEvent documentEvent) {
        logger.info("documentEvent Listener");
        try {
            Set<WebSocketSession> sessions = sessionsByProject.get(documentEvent.getProjectId());
            if (sessions != null && !sessions.isEmpty()) {

                WsMessage message = WsMessage.builder()
                        .resType(documentEvent.getResType())
                        .data(documentEvent.getData())
                        .build();

                sessions.forEach(session -> {
                    // 이벤트를 발생시킨 사용자가 아닌 팀원에게만 전송
                    if (documentEvent.getUserId() != session.getAttributes().get("userId")) {
                        if (session.isOpen()) {
                            try {
                                session.sendMessage(new TextMessage(JsonMapper.toJsonString(message)));
                            } catch (IOException e) {
                                logger.error("IOException in sendGroupEventMethod -> {}", e.getMessage());
                            }
                        }
                    }
                });
                logger.info("complete sendGroupEvent");
            }
        } catch (RuntimeException e) {
            logger.error("{} in sendGroupEventMethod -> {}", e.getClass(), e.getMessage());
        }
    }


}
