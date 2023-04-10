package com.linking.document;

import com.linking.document.service.DocumentService;
import com.linking.group.dto.GroupRes;
import com.linking.group.persistence.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DocumentHandler extends TextWebSocketHandler {

    private final DocumentService documentService;
//    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(message);
        super.handleTextMessage(session, message);
    }



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        List<GroupRes> allDocuments = documentService.findAllDocuments(2L);
        session.sendMessage(new WebSocketMessage<List<GroupRes>>() {
            @Override
            public List<GroupRes> getPayload() {
                return allDocuments;
            }

            @Override
            public int getPayloadLength() {
                return 0;
            }

            @Override
            public boolean isLast() {
                return false;
            }
        });
        super.afterConnectionEstablished(session);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
//
//    @Override
//    public void handleTransportError(WebSocketSession sesuper.handleTransportError(session, exception);ssion, Throwable exception) throws Exception {
//
//    }
}
