package com.linking.group.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.group.CustomEmitter;
import com.linking.group.dto.*;
import com.linking.group.service.GroupService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Tag(name = "Group")
@Slf4j
public class GroupController {

    private final DocumentSseHandler documentSseHandler;
    private final GroupService groupService;
    private static final Long TIMEOUT = 600 * 1000L;

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> getGroups(
            @Parameter(description = "project id", in = ParameterIn.PATH) @PathVariable("id") Long projectId,
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId) throws IOException {

        CustomEmitter customEmitter = new CustomEmitter(userId, new SseEmitter(TIMEOUT));
        documentSseHandler.connect(projectId, customEmitter);
        List<GroupDetailedRes> allGroups = groupService.findAllGroups(projectId, userId);
        customEmitter.getSseEmitter().send(SseEmitter.event()
                .name("connect")
                .data(allGroups));

        return ResponseEntity.ok(customEmitter.getSseEmitter());
    }

    @PostMapping
    @Operation(summary = "그룹 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = GroupDetailedRes.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> postGroup(
            @RequestBody @Valid GroupCreateReq req,
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId
    ) {

        GroupRes res = groupService.createGroup(req);
        documentSseHandler.send(res.getProjectId(), userId, "postGroup", res);

        return ResponseHandler.generateCreatedResponse(res);
    }

    @PutMapping
    @Operation(summary = "그룹 이름 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful -> response body 'data' : true"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> putGroupName(
            @RequestBody @Valid GroupNameReq req,
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId
    ) {

        GroupRes res = groupService.updateGroupName(req);
        documentSseHandler.send(res.getProjectId(), userId, "putGroupName", res);

        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, res);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "그룹 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> deleteGroup(
            @Parameter(description = "group id", in = ParameterIn.PATH) @PathVariable("id") Long groupId,
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId
            ) {

        Map<String, Object> result = groupService.deleteGroup(groupId);
        documentSseHandler.send((Long) result.get("projectId"), userId, "deleteGroup", result.get("data"));

        return ResponseHandler.generateNoContentResponse();
    }

    @PutMapping("/order")
    @Operation(summary = "그룹 및 페이지 순서 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful -> response body 'data' : true"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> putDocumentOrder(
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userid") Long userId,
            @RequestBody @Valid List<GroupOrderReq> req) {

        boolean res = groupService.updateDocumentsOrder(req, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, res);
    }

//    @PostMapping("/{id}")
//    @Operation(summary = "그룹 리스트 조회")
//
//    public ResponseEntity<Object> getDocuments(
//            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userid") Long userId,
//            @Parameter(description = "project id", in = ParameterIn.PATH) @PathVariable("id") Long projectId) {
//
//        List<GroupRes> documentRes = groupService.findAllGroups(projectId, userId);
//        return ResponseHandler.generateOkResponse(documentRes);
//    }
}
