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
@Tag(name = "Annotation")
public class AnnotationController {

    private final DocumentSseHandler documentSseHandler;
    private final AnnotationService annotationService;

    @PostMapping
    @Operation(summary = "주석 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = AnnotationRes.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> postAnnotation(
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId,
            @RequestBody @Valid AnnotationCreateReq req) {

        Map<String, Object> result = annotationService.createAnnotation(req, userId);
        // (sse) 주석응답을 보내주는것 X. 주석이 생성된 페이지의 id를 보내줌.
        documentSseHandler.send((Long)result.get("projectId"), userId, "postAnnotation", result.get("message"));

        return ResponseHandler.generateCreatedResponse(result.get("data"));
    }

    @PutMapping
    @Operation(summary = "주석 내용 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 완료", content = @Content(schema = @Schema(implementation = AnnotationRes.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<Object> putAnnotation(
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId,
            @RequestBody @Valid AnnotationUpdateReq req) {

        AnnotationRes annotationRes = annotationService.updateAnnotation(req, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, annotationRes);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "주석 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 완료. body 없음"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> deleteAnnotation(
            @Parameter(description = "project id", in = ParameterIn.HEADER) @RequestHeader(value = "projectId") Long projectId,
            @Parameter(description = "user id", in = ParameterIn.HEADER) @RequestHeader(value = "userId") Long userId,
            @Parameter(description = "주석 id", in = ParameterIn.PATH) @PathVariable("id") Long id) {

        Map<String, Object> result = annotationService.deleteAnnotation(id, projectId, userId);
        // (sse) 주석 삭제된 페이지의 id 전송
        documentSseHandler.send((Long) result.get("projectId"), userId, "deleteAnnotation", result.get("message"));

        return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
    }
}
