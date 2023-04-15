package com.linking.group.controller;

import com.linking.global.ResponseHandler;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupNameReq;
import com.linking.group.event.GroupEvent;
import com.linking.group.service.GroupService;
import com.linking.ws.WsResponseType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Tag(name = "Group", description = "그룹 API Document")
public class GroupController {
    Logger logger = LoggerFactory.getLogger(GroupController.class);
    private final ApplicationEventPublisher publisher;
    private final GroupService groupService;

//    // http 요청으론 안 쓰일 예정
//    @PostMapping("/{id}")
//    public ResponseEntity<Object> getGroups(@PathVariable("id") Long projectId) {
//        List<GroupRes> documentRes = groupService.findAllGroups(projectId);
//        return ResponseHandler.generateOkResponse(documentRes);
//    }


    //TODO 원래 코드
//    @RequestHeader("userid") Long userId,

    @Operation(summary = "그룹 생성")
    @PostMapping
    public ResponseEntity<Object> postGroup(
            @RequestHeader Map<String, String> headers,
            @RequestBody @Valid GroupCreateReq req) {

        try {
            Long userId = 1L;
//            Long userId = Long.parseLong(headers.get("userId"));

            GroupRes groupRes = groupService.createGroup(req);

            logger.info("GroupCreate is published");
            publisher.publishEvent(new GroupEvent(WsResponseType.GROUP, WsResponseType.CREATE, userId, groupRes.getProjectId(), groupRes));

            return ResponseHandler.generateCreatedResponse(groupRes);

        } catch (NumberFormatException e) {
            return ResponseHandler.generateResponse("userId must be NUMBER", HttpStatus.BAD_REQUEST, null);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (RuntimeException e) {
            logger.error("\n{} ===============> {}", e.getClass(), e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

//    @PutMapping("/order")
//    public ResponseEntity<Object> putDocumentOrder(@RequestBody @Valid List<GroupOrderReq> req) {
//        try {
//            groupService.updateDocumentsOrder(req);
//            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, true);
//        } catch (NoSuchElementException e) {
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
//        } catch (RuntimeException e) {
//            logger.error("{} ============> {}", e.getClass(), e.getMessage());
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
//        }
//    }

    @Operation(summary = "그룹 이름 수정")
    @PutMapping
    public ResponseEntity<Object> putGroupName(@RequestBody @Valid GroupNameReq req) {
        try {
            groupService.updateGroupName(req);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, true);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (RuntimeException e) {
            logger.error("\n{} ===============> {}", e.getClass(), e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroup(@PathVariable("id") Long groupId) {

        try {
            groupService.deleteGroup(groupId);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (RuntimeException e) {
            logger.error("\n{} ===============> {}", e.getClass(), e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public void publishEvent(int publishType, Object event) {

    }
}
