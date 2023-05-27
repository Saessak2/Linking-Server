package com.linking.socket.page.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PageSocketSessionRepositoryImpl implements IPageSocketRepository {

    private final Map<Long, Set<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    @Override
    public int save(Long pageId, WebSocketSession session) {

        Set<WebSocketSession> sessionsByPage = sessions.get(pageId);

        if (sessionsByPage == null) {
            sessionsByPage = Collections.synchronizedSet(new HashSet<>());
            sessionsByPage.add(session);
            sessions.put(pageId, sessionsByPage);
        } else {
            sessionsByPage.add(session);
        }
        return sessionsByPage.size();
    }

    @Override
    public Set<WebSocketSession> findByPageId(Long pageId) {
        return sessions.get(pageId);
    }
}
