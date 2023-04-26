package com.linking.page.controller;

import com.linking.global.CustomEmitter;
import com.linking.global.common.ResponseHandler;
import com.linking.group.controller.DocumentSseHandler;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageDetailedRes;
import com.linking.page.dto.PageRes;
import com.linking.page.service.PageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/pages")
@RequiredArgsConstructor
@Slf4j
public class PageController extends TextWebSocketHandler {

    private final DocumentSseHandler documentSseHandler;
    private final PageSseHandler pageSseHandler;
    private final PageService pageService;

    private static final Long TIMEOUT = 600 * 1000L;

    @PostMapping("/{id}")
    public ResponseEntity<SseEmitter> getPage(
            @RequestHeader(value = "userId") Long userId, @PathVariable("id") Long pageId) {

        CustomEmitter customEmitter = new CustomEmitter(userId, new SseEmitter(TIMEOUT));
        pageSseHandler.connect(pageId, customEmitter);

        PageDetailedRes res = pageService.getPage(pageId, userId);

        try {
            customEmitter.getSseEmitter().send(SseEmitter.event()
                    .name("connect")
                    .data(res)
            );
        } catch (IOException e) {
            log.error("cannot send event");
            // 연결 끊어?
        }
        return ResponseEntity.ok(customEmitter.getSseEmitter());
    }

    @PostMapping
    public ResponseEntity<Object> postPage(
            @RequestHeader(value = "userId") Long userId, @RequestBody @Valid PageCreateReq pageCreateReq
    ){

        Map<String, Object> result = pageService.createPage(pageCreateReq);
        documentSseHandler.send((Long) result.get("projectId"), userId, "postPage", result.get("data"));
        return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, result.get("data"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePage(
            @RequestHeader(value = "userId") Long userId, @PathVariable("id") Long pageId) {

        Map<String, Object> result = pageService.deletePage(pageId);
        documentSseHandler.send((Long) result.get("projectId"), userId, "deletePage", result.get("data"));
        return ResponseHandler.generateNoContentResponse();
    }
}

