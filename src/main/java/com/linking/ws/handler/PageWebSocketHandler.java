package com.linking.ws.handler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class PageWebSocketHandler extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    // 연결 중인 세션 저장
    // key -> sessionId
    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    // key -> pageId
    private static Map<Long, Set<WebSocketSession>> sessionsByProject = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }
}
