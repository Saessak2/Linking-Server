package com.linking.document.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.linking.document.service.DocumentService;
import com.linking.global.ResponseHandler;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupOrderReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.PUT})
public class DocumentController extends TextWebSocketHandler {

    private final DocumentService documentService;
    private final GroupService groupService;
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<Object> getDocuments(@PathVariable("id") Long projectId) {
        List<GroupRes> documentRes = documentService.findAllDocuments(projectId);
        return ResponseHandler.generateOkResponse(documentRes);
    }

    @PutMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.PUT})
    public ResponseEntity<Object> putDocumentOrder(@RequestBody @Valid List<GroupOrderReq> req) {
        try {
            documentService.updateDocumentsOrder(req);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, true);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    @MessageMapping(value = "/ws/groups")
    public void group(GroupCreateReq group) {
        Optional<GroupRes> groupRes = groupService.createGroup(group);
        template.convertAndSend("/sub/documents" + group.getProjectId(), groupRes);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(message);
        super.handleTextMessage(session, message);
    }



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        List<GroupRes> allDocuments = documentService.findAllDocuments(2L);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(allDocuments);
        session.sendMessage(new TextMessage(json));
//        super.afterConnectionEstablished(session);
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
