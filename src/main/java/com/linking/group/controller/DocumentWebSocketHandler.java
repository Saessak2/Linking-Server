package com.linking.group.controller;

import com.linking.ws.event.DocumentEvent;
import com.linking.global.util.JsonMapper;
import com.linking.ws.message.WsMessage;
import com.linking.group.service.WsDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DocumentWebSocketHandler extends TextWebSocketHandler {

    /**
     * 연결 중인 세션 저장
     * key : (Long) project id
     * value : Set<WebSocketSession>
     */
    private static Map<Long, Set<WebSocketSession>> sessionsByProject = new ConcurrentHashMap<>();

    private final WsDocumentService wsService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        Long projectId = (Long) session.getAttributes().get("projectId");
        Long userId = (Long) session.getAttributes().get("userId");

        Set<WebSocketSession> sessions = this.addSession(projectId, session);

        // 문서 리스트 전송
        try {
            session.sendMessage(wsService.getAllDocuments(projectId, userId));
        } catch (IOException e) {
            log.info(e.getMessage());
        }

        log.info("@@ [DOC][CONNECT] @@ projectId = {} @@ userId = {}" , projectId, userId);
        log.info("@@ [DOC][SESSIONS] @@ projectId = {} @@ sessions.size = {}", projectId, sessions.size());
    }

    private Set<WebSocketSession> addSession(Long key, WebSocketSession session) {

        Set<WebSocketSession> sessions = sessionsByProject.get(key);

        if (sessions == null) {
            sessions = Collections.synchronizedSet(new HashSet<>());
            sessions.add(session);
            sessionsByProject.put(key, sessions);

        } else {
            sessions.add(session);
        }
        return sessions;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

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
//            log.error("Exception while ping session");
//        }
//    }
//
//    @Override
//    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
////        logger.info("receive pong from session.id = {}", session.getId());
//    }

    private void close(WebSocketSession session) throws IOException {

        Long projectId = (Long) session.getAttributes().get("projectId");
        session.close();
        Set<WebSocketSession> sessions = sessionsByProject.get(projectId);
        if (sessions != null)
            sessions.remove(session);

        log.info("@@ [DOC][CLOSE] @@ projectId = {} @@ userId = {}" , projectId, session.getAttributes().get("userId"));
        log.info("@@ [DOC][SESSIONS] @@ projectId = {} @@ session size = {}", projectId, sessions.size());
    }


    @EventListener
    public void sendGroups(com.linking.group.dto.DocumentEvent documentEvent) {
        log.info("event listener");
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
        log.info("documentEvent Listener");
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
                                log.error("IOException in sendGroupEventMethod -> {}", e.getMessage());
                            }
                        }
                    }
                });
                log.info("complete sendGroupEvent");
            }
        } catch (RuntimeException e) {
            log.error("{} in sendGroupEventMethod -> {}", e.getClass(), e.getMessage());
        }
    }


}
