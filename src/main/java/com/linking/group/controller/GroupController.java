package com.linking.group.controller;

import com.linking.domain.group.dto.*;
import com.linking.group.service.GroupService;
import com.linking.global.common.Login;
import com.linking.global.common.ResponseHandler;
import com.linking.global.common.UserCheck;
import com.linking.group.dto.*;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {
    private final GroupSseHandler groupSseHandler;
    private final GroupService groupService;

    @GetMapping("/list")
    public ResponseEntity<List<GroupDetailedRes>> getGroups(
            @RequestParam("projectId") Long projectId,
            @Login UserCheck userCheck
    ){
        List<GroupDetailedRes> allGroups = groupService.findAllGroups(projectId, userCheck.getUserId());
        return ResponseHandler.generateOkResponse(allGroups);
    }

    @GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribeGroup(
            @RequestParam("projectId") Long projectId,
            @Login UserCheck userCheck
    ){
        SseEmitter sseEmitter = groupSseHandler.connect(projectId, userCheck.getUserId());
        try {
            sseEmitter.send(SseEmitter.event().name("connect").data("connected!"));
        } catch (IOException e) {
            log.error("cannot send event");
        }
        return ResponseEntity.ok(sseEmitter);
    }

    @PostMapping
    public ResponseEntity<Object> postGroup(
            @RequestBody @Valid GroupCreateReq req,
            @Login UserCheck userCheck
    ){
        GroupRes res = groupService.createGroup(req, userCheck.getUserId());
        return ResponseHandler.generateCreatedResponse(res);
    }

    @PutMapping
    public ResponseEntity<Object> putGroupName(
            @RequestBody @Valid GroupNameReq req,
            @Login UserCheck userCheck
    ) {

        Boolean res = groupService.updateGroupName(req, userCheck.getUserId());
        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroup(
            @PathVariable("id") Long groupId,
            @Login UserCheck userCheck
    ) {

        groupService.deleteGroup(groupId, userCheck.getUserId());

        return ResponseHandler.generateNoContentResponse();
    }

    @PutMapping("/order")
    public ResponseEntity<Object> putDocumentOrder(
            @RequestBody @Valid List<GroupOrderReq> req,
            @Login UserCheck userCheck
    ) {

        boolean res = groupService.updateDocumentsOrder(req, userCheck.getUserId());
        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, res);
    }
}