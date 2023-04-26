package com.linking.group.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.global.CustomEmitter;
import com.linking.group.dto.*;
import com.linking.group.service.GroupService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@Slf4j
public class GroupController {

    private final DocumentSseHandler documentSseHandler;
    private final GroupService groupService;
    private static final Long TIMEOUT = 600 * 1000L;

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> getGroups(
            @Parameter(description = "project id", in = ParameterIn.PATH) @PathVariable("id") Long projectId,
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId)
    {

        CustomEmitter customEmitter = new CustomEmitter(userId, new SseEmitter(TIMEOUT));
        documentSseHandler.connect(projectId, customEmitter);

        List<GroupDetailedRes> allGroups = groupService.findAllGroups(projectId, userId);

        try {
            customEmitter.getSseEmitter().send(SseEmitter.event()
                    .name("connect")
                    .data(allGroups));
        } catch (IOException e) {
            log.error("cannot send event");
        }

        return ResponseEntity.ok(customEmitter.getSseEmitter());
    }

    @PostMapping
    public ResponseEntity<Object> postGroup(
            @RequestBody @Valid GroupCreateReq req, @RequestHeader(value = "userId") Long userId
    ) {

        GroupRes res = groupService.createGroup(req);
        documentSseHandler.send(res.getProjectId(), userId, "postGroup", res);

        return ResponseHandler.generateCreatedResponse(res);
    }

    @PutMapping
    public ResponseEntity<Object> putGroupName(
            @RequestBody @Valid GroupNameReq req, @RequestHeader(value = "userId") Long userId
    ) {

        GroupRes res = groupService.updateGroupName(req);
        documentSseHandler.send(res.getProjectId(), userId, "putGroupName", res);

        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroup(
            @PathVariable("id") Long groupId, @RequestHeader(value = "userId") Long userId) {

        Map<String, Object> result = groupService.deleteGroup(groupId);
        documentSseHandler.send((Long) result.get("projectId"), userId, "deleteGroup", result.get("data"));

        return ResponseHandler.generateNoContentResponse();
    }

    @PutMapping("/order")
    public ResponseEntity<Object> putDocumentOrder(
            @RequestHeader(value = "userid") Long userId, @RequestBody @Valid List<GroupOrderReq> req) {

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
