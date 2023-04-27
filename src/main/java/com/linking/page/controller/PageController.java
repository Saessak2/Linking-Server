package com.linking.page.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageDetailedRes;
import com.linking.page.dto.PageRes;
import com.linking.page.service.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/pages")
@RequiredArgsConstructor
@Slf4j
public class PageController extends TextWebSocketHandler {

    private final PageSseHandler pageSseHandler;
    private final PageService pageService;

    private static final Long TIMEOUT = 600 * 1000L;

    @PostMapping("/{id}")
    public ResponseEntity<SseEmitter> getPage(
            @RequestHeader(value = "userId") Long userId, @PathVariable("id") Long pageId) {

        SseEmitter sseEmitter = pageSseHandler.connect(pageId, userId);

        PageDetailedRes res = pageService.getPage(pageId, userId, pageSseHandler.getUserIdsByPage(pageId));

        try {
            sseEmitter.send(SseEmitter.event()
                    .name("connect")
                    .data(res)
            );
        } catch (IOException e) {
            log.error("cannot send event");
            // 연결 끊어?
        }
        return ResponseEntity.ok(sseEmitter);
    }

    @PostMapping
    public ResponseEntity<Object> postPage(
            @RequestHeader(value = "userId") Long userId, @RequestBody @Valid PageCreateReq pageCreateReq
    ){

        PageRes res = pageService.createPage(pageCreateReq, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePage(
            @RequestHeader(value = "userId") Long userId, @PathVariable("id") Long pageId) {

        pageService.deletePage(pageId, userId);
        return ResponseHandler.generateNoContentResponse();
    }

    @PostMapping("/onclose/{id}")
    public void leavePage(@RequestHeader(value = "userId") Long userId, @PathVariable("id") Long pageId) {
//        pageService.update
    }
}

