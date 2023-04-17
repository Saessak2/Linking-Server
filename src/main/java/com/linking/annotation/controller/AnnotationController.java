package com.linking.annotation.controller;

import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateReq;
import com.linking.annotation.service.AnnotationService;
import com.linking.global.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.Implementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/annotations")
@RequiredArgsConstructor
@Tag(name = "주석")
public class AnnotationController {

    private final AnnotationService annotationService;

//    @GetMapping("/{id}")
//    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
//    public ResponseEntity<Object> getAnnotations(@PathVariable("id") Long blockId) {
//
//        return ResponseHandler.generateOkResponse(annotationService.findAnnotations(blockId));
//    }


    @PostMapping
    @Operation(summary = "주석 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = AnnotationRes.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> postAnnotation(@RequestBody @Valid AnnotationCreateReq req) {
        AnnotationRes res = annotationService.createAnnotation(req);
        return ResponseHandler.generateCreatedResponse(res);
    }

    @PutMapping
    @Operation(summary = "주석 내용 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경 완료"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<Object> putAnnotation(@RequestBody @Valid AnnotationUpdateReq req) {
        AnnotationRes annotationRes = annotationService.updateAnnotation(req);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, annotationRes);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "주석 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 완료. body 없음"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Object> deleteAnnotation(@Parameter(description = "주석 id", in = ParameterIn.PATH) @PathVariable("id") Long id) {
        annotationService.deleteAnnotation(id);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
    }
}
