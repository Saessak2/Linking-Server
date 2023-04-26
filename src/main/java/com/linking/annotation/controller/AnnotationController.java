package com.linking.annotation.controller;

import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateReq;
import com.linking.annotation.service.AnnotationService;
import com.linking.global.common.ResponseHandler;
import com.linking.group.controller.DocumentSseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/annotations")
@RequiredArgsConstructor
public class AnnotationController {

    private final DocumentSseHandler documentSseHandler;
    private final AnnotationService annotationService;

    @PostMapping
    public ResponseEntity<Object> postAnnotation(
            @RequestHeader(value = "userId") Long userId, @RequestBody @Valid AnnotationCreateReq req) {

        Map<String, Object> result = annotationService.createAnnotation(req, userId);
        // (sse) 주석응답을 보내주는것 X. 주석이 생성된 페이지의 id를 보내줌.
        documentSseHandler.send((Long)result.get("projectId"), userId, "postAnnotation", result.get("message"));

        return ResponseHandler.generateCreatedResponse(result.get("data"));
    }

    @PutMapping
    public ResponseEntity<Object> putAnnotation(
            @RequestHeader(value = "userId") Long userId, @RequestBody @Valid AnnotationUpdateReq req) {

        AnnotationRes annotationRes = annotationService.updateAnnotation(req, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, annotationRes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAnnotation(
            @RequestHeader(value = "projectId") Long projectId,
            @RequestHeader(value = "userId") Long userId,
            @PathVariable("id") Long id) {

        Map<String, Object> result = annotationService.deleteAnnotation(id, projectId, userId);
        // (sse) 주석 삭제된 페이지의 id 전송
        documentSseHandler.send((Long) result.get("projectId"), userId, "deleteAnnotation", result.get("message"));

        return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
    }
}
