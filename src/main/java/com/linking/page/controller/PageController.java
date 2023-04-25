package com.linking.page.controller;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/pages")
@RequiredArgsConstructor
@Tag(name = "Page")
@Slf4j
public class PageController extends TextWebSocketHandler {

    private final DocumentSseHandler documentSseHandler;
    private final PageService pageService;


    @PostMapping("/{id}")
    @Operation(summary = "페이지 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful", content = @Content(schema = @Schema(implementation = PageDetailedRes.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> getPage(
            @Parameter(in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId,
            @Parameter(in = ParameterIn.PATH) @PathVariable("id") Long pageId) {

        PageDetailedRes res = pageService.getPage(pageId, userId);
        return ResponseHandler.generateOkResponse(res);
    }

    @PostMapping
    @Operation(summary = "페이지 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = PageRes.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> postPage(
            @Parameter(in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId,
            @RequestBody @Valid PageCreateReq pageCreateReq
    ){

        Map<String, Object> result = pageService.createPage(pageCreateReq);
        documentSseHandler.send((Long) result.get("projectId"), userId, "postPage", result.get("data"));
        return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, result.get("data"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "페이지 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> deletePage(
            @Parameter(in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId,
            @Parameter(description = "pageId") @PathVariable("id") Long pageId) {

        Map<String, Object> result = pageService.deletePage(pageId);
        documentSseHandler.send((Long) result.get("projectId"), userId, "deletePage", result.get("data"));
        return ResponseHandler.generateNoContentResponse();
    }
}

