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
public class PageController extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(PageController.class);
    private final PageService pageService;


    @PostMapping("/{id}")
    public ResponseEntity<Object> getPage(
            @PathVariable("id") Long pageId, @RequestParam("userId") Long userId) {
        try {
            return pageService.getPage(pageId, userId)
                    .map(ResponseHandler::generateOkResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
        } catch (NoSuchElementException e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping
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
    public ResponseEntity<Object> deletePage(@PathVariable("id") Long docId) {
        try {
            pageService.deletePage(docId);
            return ResponseHandler.generateNoContentResponse();
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (RuntimeException e) {
            logger.error("\n{} ===============> {}", e.getClass(), e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}

