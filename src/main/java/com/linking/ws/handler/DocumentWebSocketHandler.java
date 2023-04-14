package com.linking.ws.handler;

import com.linking.util.JsonMapper;
import com.linking.ws.service.WsDocumentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static Map<Long, Set<WebSocketSession>> sessionsByProject = new ConcurrentHashMap<>();

    private final WsDocumentService wsService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("ws -> session.id = {} connection established" , session );
        logger.info("ws -> session.getAttribute : {}", session.getAttributes().get("projectId"));

        Long key = (Long) session.getAttributes().get("projectId");
        // 즉시 전체 문서 리스트 전송
        session.sendMessage(new TextMessage(JsonMapper.toJsonString(wsService.getAllDocumentsByProjectId(key))));

//        long beforeTime = System.currentTimeMillis();

        if (!sessionsByProject.containsKey(key)) {
            Set<WebSocketSession> hashSet = Collections.synchronizedSet(new HashSet<>());
            hashSet.add(session);
            sessionsByProject.put(key, hashSet);
        } else {
            if (!sessionsByProject.get(key).contains(session)) {
                sessionsByProject.get(key).add(session);
            }
        }

        if (!sessions.containsKey(session.getId())) {
            sessions.put(session.getId(), session);
        }

//        long afterTime = System.currentTimeMillis();
//        logger.info("session 저장하는 데 걸린 시간 (m) : ", (afterTime-beforeTime)/1000);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("ws -> session.id = {} closed ... and status is = {}", session.getId(), status);
        sessions.remove(session.getId());
        // TODO sessionByProject에서도 삭제하기
    }

//
//    @EventListener
//    public void sendGroups(DocumentEvent documentEvent) {
//        logger.info("event listener");
////        logger.info("currentThread =====+++> {}", Thread.currentThread().getName());
//
//        Set<WebSocketSession> webSocketSessions = sessionsByProject.get(documentEvent.getProjectId());
//        for (WebSocketSession session : webSocketSessions) {
//            try {
//                if (session.isOpen())
//                    session.sendMessage(new TextMessage(JsonMapper.toJsonString(documentEvent.getGroupResList())));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

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
//                    logger.info("session is opened");
                }
                else {
                    sessions.remove(session.getId());
//                    logger.info("session is closed");
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
