package com.linking.ws.handler;

import com.linking.group.dto.DocumentEvent;
import com.linking.group.event.GroupEvent;
import com.linking.util.JsonMapper;
import com.linking.ws.WsResponseType;
import com.linking.ws.message.WsMessage;
import com.linking.ws.service.WsDocumentService;
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
public class DocumentWebSocketHandler extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    // 연결 중인 세션 저장
    // ket -> sessionId
    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    // key -> projectId
    private static Map<Long, Set<WebSocketSession>> sessionsByProject = new ConcurrentHashMap<>();

    private final WsDocumentService wsService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        logger.info("@@ connect session.id = {} @@ projectId = {}" , session, session.getAttributes().get("projectId") );

        Long projectIdKey = (Long) session.getAttributes().get("projectId");
        Long userId = (Long) session.getAttributes().get("userId");

        // 즉시 전체 문서 리스트 전송
        session.sendMessage(new TextMessage(JsonMapper.toJsonString(wsService.getAllDocumentsByProjectAndUser(projectIdKey, userId))));

//        long beforeTime = System.currentTimeMillis();

        if (!sessionsByProject.containsKey(projectIdKey)) {
            Set<WebSocketSession> hashSet = Collections.synchronizedSet(new HashSet<>());
            hashSet.add(session);
            sessionsByProject.put(projectIdKey, hashSet);
        } else {
            if (!sessionsByProject.get(projectIdKey).contains(session)) {
                sessionsByProject.get(projectIdKey).add(session);
            }
        }
        if (!sessions.containsKey(session.getId())) {
            sessions.put(session.getId(), session);
        }

        logger.info("@@ sessionsByProjects -> projectId = {}, size = {}", projectIdKey, sessionsByProject.get(projectIdKey).size());
        logger.info("@@ sessions -> size = {}", sessions.size());
//        long afterTime = System.currentTimeMillis();
//        logger.info("session 저장하는 데 걸린 시간 (m) : ", (afterTime-beforeTime)/1000);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("@@ ws -> session.id = [{}] closed ... and status is = {}", session.getId(), status);
        close(session);

        // TODO sessionByProject에서도 삭제하기
    }

    @Scheduled(cron = "0/30 * * * * *")
    public void ping() {
//        logger.info("현재 쓰레드 : {}", Thread.currentThread().getName());
        try {
            for (WebSocketSession session : sessions.values()) {
                if (session.isOpen()) {
                    session.sendMessage(new PingMessage());
//                    logger.info("session is opened");
                }
                else {
                    close(session);
                }
            }
        } catch (IOException e) {
            logger.error("Exception while ping session");
        }
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
//        logger.info("receive pong from session.id = {}", session.getId());

    }

    private void close(WebSocketSession session) throws IOException {
        Long projectId = (Long) session.getAttributes().get("projectId");
        session.close();
        sessions.remove(session.getId());
        sessionsByProject.get(projectId).remove(session);
        logger.info("@@ sessionsByProjects -> projectId = {}, size = {}", projectId, sessionsByProject.get(projectId).size());
        logger.info("@@ sessions -> size = {}", sessions.size());
    }


    @EventListener
    public void sendGroups(DocumentEvent documentEvent) {
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
    public void sendGroupEvent(GroupEvent groupEvent) {
        logger.info("groupEvent Listener");
        try {
            Set<WebSocketSession> sessions = sessionsByProject.get(groupEvent.getProjectId());
            if (sessions != null && !sessions.isEmpty()) {
                WsMessage message = WsMessage.builder()
                        .resType(groupEvent.getResType())
                        .publishType(groupEvent.getPublishType())
                        .data(groupEvent.getGroupRes())
                        .build();
                sessions.forEach(session -> {
                    if (groupEvent.getUserId() != session.getAttributes().get("userId")) {
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
}
