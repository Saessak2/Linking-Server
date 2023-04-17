package com.linking.group.controller;

import com.linking.global.ResponseHandler;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupNameReq;
import com.linking.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Tag(name = "Group", description = "그룹 API Document")
public class GroupController {
    Logger logger = LoggerFactory.getLogger(GroupController.class);
    private final GroupService groupService;

    @Operation(summary = "그룹 생성")
    @PostMapping
    public ResponseEntity<Object> postGroup(
            @RequestHeader(value = "userid", required = false) Long userId,
            @RequestBody @Valid GroupCreateReq req) {

        GroupRes groupRes = groupService.createGroup(req, userId);
        logger.info("GroupCreate is published");

        return ResponseHandler.generateCreatedResponse(groupRes);
    }

    @Operation(summary = "그룹 이름 수정")
    @PutMapping
    public ResponseEntity<Object> putGroupName(
            @RequestHeader(value = "userid", required = false) Long userId,
            @RequestBody @Valid GroupNameReq req) {

        boolean res = groupService.updateGroupName(req, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroup(
            @RequestHeader(value = "userid", required = false) Long userId,
            @PathVariable("id") Long groupId) {

        boolean res = groupService.deleteGroup(groupId, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, res);
    }
}
