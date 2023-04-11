package com.linking.document.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.block.service.BlockService;
import com.linking.document.service.DocumentService;
import com.linking.global.ResponseHandler;
import com.linking.group.dto.GroupOrderReq;
import com.linking.group.dto.GroupRes;
import com.linking.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/documents")
@RequiredArgsConstructor
public class DocumentController extends TextWebSocketHandler {

    private final DocumentService documentService;


    Logger logger = LoggerFactory.getLogger(DocumentController.class);


    @PostMapping("/{id}")
    public ResponseEntity<Object> getDocuments(@PathVariable("id") Long projectId) {
        List<GroupRes> documentRes = documentService.findAllDocuments(projectId);
        return ResponseHandler.generateOkResponse(documentRes);
    }

    @PutMapping
    public ResponseEntity<Object> putDocumentOrder(@RequestBody @Valid List<GroupOrderReq> req) {
        try {
            documentService.updateDocumentsOrder(req);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, true);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (RuntimeException e) {
            logger.error("{} ============> {}", e.getClass(), e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

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
//
}
