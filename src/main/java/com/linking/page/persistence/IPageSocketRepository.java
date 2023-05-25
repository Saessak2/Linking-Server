package com.linking.page.persistence;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public interface IPageSocketRepository {

    /**
     * @return size of sessionByPage
     */
    int save(Long pageId, WebSocketSession session);


}
