package com.linking.document.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.group.dto.GroupOrderReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
    @Operation(summary = "그룹 리스트 조회")
    public ResponseEntity<Object> getDocuments(
            @RequestHeader(value = "userid", required = false) Long userId,
            @Parameter(description = "프로젝트 id", in = ParameterIn.PATH) @PathVariable("id") Long projectId) {
        List<GroupRes> documentRes = groupService.findAllGroups(projectId, userId);
        return ResponseHandler.generateOkResponse(documentRes);
    }

    @PutMapping
    @Operation(summary = "그룹 및 페이지 순서 변경")
    public ResponseEntity<Object> putDocumentOrder(
            @RequestHeader(value = "userid", required = false) Long userId,
            @RequestBody @Valid List<GroupOrderReq> req) {
        groupService.updateDocumentsOrder(req, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, true);
    }
}
