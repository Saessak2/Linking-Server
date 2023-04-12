package com.linking.document.controller;

import com.linking.document.service.DocumentService;
import com.linking.group.dto.GroupRes;
import com.linking.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;


@RequiredArgsConstructor
public class DocumentWsHandler extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(DocumentWsHandler.class);

    private final DocumentService documentService;


    /**
     * websocket
     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        session.sendMessage(message);
//        super.handleTextMessage(session, message);
//    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("\nafterConnectionEstablished : {}" , session );


        List<GroupRes> allDocuments = documentService.findAllDocuments(2L);
        session.sendMessage(new TextMessage(JsonMapper.toJsonString(allDocuments)));
    }

    //    @Override
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//        super.handleMessage(session, message);
//    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("\nsessionId ========> {}", session.getId());
        logger.info("\ncloseStatus ======> {}", status);
        super.afterConnectionClosed(session, status);
    }
}
