package com.linking.page.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageDetailedRes;
import com.linking.page.dto.PageRes;
import com.linking.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.Valid;

@RestController
@RequestMapping("/pages")
@RequiredArgsConstructor
public class PageController extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(PageController.class);
    private final PageService pageService;


    @PostMapping("/{id}")
    public ResponseEntity<Object> getPage(
            @PathVariable("id") Long pageId, @RequestParam("userId") Long userId) {

        PageDetailedRes res = pageService.getPage(pageId, userId);
        return ResponseHandler.generateOkResponse(res);
    }

    @PostMapping
    public ResponseEntity<Object> postPage(
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestBody @Valid PageCreateReq pageCreateReq) {

        PageRes res = pageService.createPage(pageCreateReq, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePage(
            @RequestHeader(value = "userId", required = false) Long userId,
            @PathVariable("id") Long docId) {
        pageService.deletePage(docId, userId);
        return ResponseHandler.generateNoContentResponse();
    }
}

