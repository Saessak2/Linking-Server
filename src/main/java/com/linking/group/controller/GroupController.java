package com.linking.group.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupOrderReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupNameReq;
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

    private final SseEmitters sseEmitters;
    private final GroupService groupService;
    /**
     * 연결 중인 세션 저장
     * key : (Long) project id
     * value : Set<WebSocketSession>
     */


    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> getDocuments(
            @Parameter(description = "project id", in = ParameterIn.PATH) @PathVariable("id") Long projectId,
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId
    ) {
        log.info("@@ [DOC][CONNECT] @@ projectId = {}", projectId);
        SseEmitter emitter = new SseEmitter(30 * 1000L);  // timeout -> 30s. 만료시간이 되면 브라우저에서 자동으로 서버에 재연결 요청을 보냄. default -> 30s
        sseEmitters.add(projectId, emitter);

        // 그룹 리스트 조회
        List<GroupRes> allGroups = groupService.findAllGroups(projectId, userId);

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data(allGroups));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }

    @PostMapping
    @Operation(summary = "그룹 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = GroupRes.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> postGroup(
            @RequestBody @Valid GroupCreateReq req) {

        GroupRes groupRes = groupService.createGroup(req);
        sseEmitters.send(groupRes.getProjectId(), "postGroup", groupRes);

        return ResponseHandler.generateCreatedResponse(groupRes);
    }

    @PutMapping
    @Operation(summary = "그룹 이름 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful -> response body 'data' : true"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> putGroupName(
            @RequestBody @Valid GroupNameReq req) {

        boolean res = groupService.updateGroupName(req);
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
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userid") Long userId,
            @Parameter(description = "group id", in = ParameterIn.PATH) @PathVariable("id") Long groupId) {

        boolean res = groupService.deleteGroup(groupId, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, res);
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
