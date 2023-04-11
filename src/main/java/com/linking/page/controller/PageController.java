package com.linking.page.controller;

import com.linking.block.service.BlockService;
import com.linking.global.ResponseHandler;
import com.linking.group.dto.GroupRes;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageDetailedRes;
import com.linking.page.service.PageService;
import com.linking.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/pages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class PageController extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(PageController.class);

    private final PageService pageService;


    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<Object> getPage(
            @PathVariable("id") Long pageId,
            @RequestParam("userId") Long userId
            )
    {
        try {
            PageDetailedRes pageDetailedRes = pageService.getPage(pageId, userId);
            if (pageDetailedRes == null)
                return ResponseHandler.generateInternalServerErrorResponse();
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, pageDetailedRes);
        } catch (NoSuchElementException e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<Object> postPage(@RequestBody @Valid PageCreateReq pageCreateReq) {
        try {
            PageDetailedRes pageDetailedRes = pageService.createPage(pageCreateReq);
            if (pageDetailedRes == null)
                return ResponseHandler.generateInternalServerErrorResponse();
            return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, pageDetailedRes);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.DELETE})
    public ResponseEntity<Object> deletePage(@PathVariable("id") Long docId) {
        try {
            pageService.deletePage(docId);
            return ResponseHandler.generateNoContentResponse();
        } catch (RuntimeException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }


    /**
     * websocket
     */

    private List<WebSocketSession> sessions = new ArrayList<>();
    private Map<String, WebSocketSession> userSessions = new HashMap<>();


//    @Scheduled(fixedRate =1000)
//    public void expire() {
//        sessions.values()
//
//    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("\nafterConnectionEstablished : {}" , session);
        Map<String, Object> attributes = session.getAttributes();
        logger.info("\nattributes ======++> {}", attributes);

        PageDetailedRes page = pageService.getPage(44L, 3L);
        session.sendMessage(new TextMessage(JsonMapper.toJsonString(page)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(message);
        super.handleTextMessage(session, message);
    }





}

