package com.linking.page.controller;

import com.linking.page.service.PageService;
import com.linking.page_check.service.PageCheckService;
import com.linking.global.auth.Login;
import com.linking.global.common.ResponseHandler;
import com.linking.global.auth.UserCheck;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageDetailedRes;
import com.linking.page.dto.PageRes;
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
    private final PageCheckService pageCheckService;

    @GetMapping("/{id}")
    public ResponseEntity<PageDetailedRes> getPage(
            @RequestHeader(value = "projectId") Long projectId,
            @PathVariable("id") Long pageId,
            @Login UserCheck userCheck
    ) {
        log.info("getPage - async test" + Thread.currentThread());
        pageCheckService.updatePageChecked(pageId, projectId, userCheck.getUserId(), "enter");
        PageDetailedRes res = pageService.getPage(pageId, pageSseHandler.enteringUserIds(pageId));
        return ResponseHandler.generateOkResponse(res);
    }

    @GetMapping("/subscribe/{id}")
    public ResponseEntity<SseEmitter> subscribePage(
            @PathVariable("id") Long pageId,
            @Login UserCheck userCheck
    ) {
        log.info("subscribe Page - async test" + Thread.currentThread());

        SseEmitter sseEmitter = pageSseHandler.connect(pageId, userCheck.getUserId());
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!")
            );
            log.info("** send connect event userID = {}", userCheck.getUserId());
        } catch (IOException e) {
            log.error("cannot send event");
        }
        return ResponseEntity.ok(sseEmitter);
    }

    @PostMapping
    public ResponseEntity<Object> postPage(
            @RequestBody @Valid PageCreateReq pageCreateReq,
            @Login UserCheck userCheck
    ) {
        PageRes res = pageService.createPage(pageCreateReq, userCheck.getUserId());
        return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePage(
            @PathVariable("id") Long pageId,
            @Login UserCheck userCheck
    ) {
        pageService.deletePage(pageId, userCheck.getUserId());
        pageSseHandler.removeEmittersByPage(pageId);
        return ResponseHandler.generateNoContentResponse();
    }

    @GetMapping("/unsubscribe/{id}")
    public void unsubscribePage(
            @RequestHeader(value = "projectId") Long projectId,
            @PathVariable("id") Long pageId,
            @Login UserCheck userCheck
    ) {
        pageSseHandler.onClose(userCheck.getUserId(), pageId);
        pageCheckService.updatePageChecked(pageId, projectId, userCheck.getUserId(), "leave");
    }
}

