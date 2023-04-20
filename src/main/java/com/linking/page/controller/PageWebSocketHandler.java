package com.linking.page.controller;

import com.linking.global.util.JsonMapper;
import com.linking.ws.event.PageCheckEvent;
import com.linking.ws.event.PageEvent;
import com.linking.ws.message.WsMessage;
import com.linking.ws.service.WsPageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class PageWebSocketHandler extends TextWebSocketHandler {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WsPageService wsPageService;


    // 연결 중인 세션 저장
    // key -> pageId
    private static Map<Long, Set<WebSocketSession>> sessionsByPage = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session)  {
        logger.info("@@ [PAGE] connect session.id = {} @@ pageId = {}" , session, session.getAttributes().get("pageId") );
        Long pageIdKey = (Long) session.getAttributes().get("pageId");
        Long userId = (Long) session.getAttributes().get("userId");
        Long projectId = (Long) session.getAttributes().get("projectId");

        if (!sessionsByPage.containsKey(pageIdKey)) {
            Set<WebSocketSession> hashSet = Collections.synchronizedSet(new HashSet<>());
            hashSet.add(session);
            sessionsByPage.put(pageIdKey, hashSet);
        } else {
            if (!sessionsByPage.get(pageIdKey).contains(session))
                sessionsByPage.get(pageIdKey).add(session);
        }
        logger.info("@@ [PAGE] sessionsByPage -> pageId = {}, size = {}", pageIdKey, sessionsByPage.get(pageIdKey).size());

        // 페이지 조회 중인 사용자
        List<Long> enteringUsers = new ArrayList<>();
        sessionsByPage.get(pageIdKey).forEach(s -> {
            enteringUsers.add((Long) s.getAttributes().get("userId"));
        });
        WsMessage page = wsPageService.getPage(pageIdKey, projectId, userId, enteringUsers);
        // 페이지 전송
        try {
            session.sendMessage(new TextMessage(JsonMapper.toJsonString(page)));
        } catch (IOException e) {
        logger.error("JsonMapper.toJsonString IOException");
        }

        // 페이지 조회 중인 다른 사용자한테 업데이트된 페이지 체크 리스트를 보내줘야함.
//        리스트?를 보내줘야하나..? 들어온 사람꺼 하나만 보내주면 안되나?
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("@@ [PAGE] -> session.id = [{}] closed ... and status is = {}", session.getId(), status);
        close(session);
    }

    private void close(WebSocketSession session) throws IOException {
        Long pageId = (Long) session.getAttributes().get("pageId");
        session.close();
        // 페이지 마지막 확인 시간 업뎃
        WsMessage message = wsPageService.updatePageLastChecked(pageId, (Long) session.getAttributes().get("projectId"), (Long) session.getAttributes().get("userId"));
        Set<WebSocketSession> webSocketSessions = sessionsByPage.get(pageId);
        if (webSocketSessions != null && session != null) {
            webSocketSessions.remove(session);
        }
        if (message != null) {
            webSocketSessions.forEach(s -> {
                try {
                    s.sendMessage(new TextMessage(JsonMapper.toJsonString(message)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        logger.info("@@ [PAGE] sessionsByPage -> pageId = {}, size = {}", pageId, webSocketSessions.size());
    }

    @EventListener
    public void sendEvent(PageEvent pageEvent) {
        logger.info("pageEvent Listener");
        try {
            Set<WebSocketSession> sessions = sessionsByPage.get(pageEvent.getPageId());
            if (sessions != null && !sessions.isEmpty()) {

                WsMessage message = WsMessage.builder()
                        .resType(pageEvent.getResType())
                        .data(pageEvent.getData())
                        .build();

                sessions.forEach(session -> {
                    // 이벤트를 발생시킨 사용자가 아닌 팀원에게만 전송
                    if (pageEvent.getUserId() != session.getAttributes().get("userId")) {
                        if (session.isOpen()) {
                            try {
                                session.sendMessage(new TextMessage(JsonMapper.toJsonString(message)));
                            } catch (IOException e) {
                                logger.error("IOException in sendPageEventMethod -> {}", e.getMessage());
                            }
                        }
                    }
                });
                logger.info("complete sendPageEvent");
            }
        } catch (RuntimeException e) {
            logger.error("{} in sendGroupEventMethod -> {}", e.getClass(), e.getMessage());
        }
    }

    @EventListener
    public void pageCheckEvent(PageCheckEvent pageCheckEvent) {
        List<Long> userIds = new ArrayList<>();

        // null확인
        sessionsByPage.get(pageCheckEvent.getPageId()).forEach(s -> {
                    userIds.add((Long) s.getAttributes().get("userId"));
                }
        );
        if (!userIds.isEmpty())
            wsPageService.sendUserIds(userIds);
    }
}
