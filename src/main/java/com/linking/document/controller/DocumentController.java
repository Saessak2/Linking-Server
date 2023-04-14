package com.linking.document.controller;

import com.linking.global.ResponseHandler;
import com.linking.group.dto.GroupOrderReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/documents")
@RequiredArgsConstructor
public class DocumentController {
    Logger logger = LoggerFactory.getLogger(DocumentController.class);

    private final GroupService groupService;


    // http 요청으론 안 쓰일 예정
    @PostMapping("/{id}")
    public ResponseEntity<Object> getDocuments(@PathVariable("id") Long projectId) {
        List<GroupRes> documentRes = groupService.findAllGroups(projectId);
        return ResponseHandler.generateOkResponse(documentRes);
    }

    @PutMapping
    public ResponseEntity<Object> putDocumentOrder(@RequestBody @Valid List<GroupOrderReq> req) {
        try {
            groupService.updateDocumentsOrder(req);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, true);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (RuntimeException e) {
            logger.error("{} ============> {}", e.getClass(), e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
