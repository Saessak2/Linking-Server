package com.linking.group.controller;

import com.linking.global.ResponseHandler;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupOrderReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupNameReq;
import com.linking.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    Logger logger = LoggerFactory.getLogger(GroupController.class);
    private final GroupService groupService;

//    // http 요청으론 안 쓰일 예정
//    @PostMapping("/{id}")
//    public ResponseEntity<Object> getGroups(@PathVariable("id") Long projectId) {
//        List<GroupRes> documentRes = groupService.findAllGroups(projectId);
//        return ResponseHandler.generateOkResponse(documentRes);
//    }


    @PostMapping
    public ResponseEntity<Object> postGroup(@RequestBody @Valid GroupCreateReq req) {
        try {
            return groupService.createGroup(req)
                    .map(ResponseHandler::generateCreatedResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
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
}
