package com.linking.annotation.controller;

import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateReq;
import com.linking.annotation.service.AnnotationService;
import com.linking.global.ErrorMessage;
import com.linking.global.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/annotations")
@RequiredArgsConstructor
public class AnnotationController {

    private final AnnotationService annotationService;

    @PostMapping
    public ResponseEntity<Object> postAnnotation(@RequestBody @Valid AnnotationCreateReq annotationCreateReq) {

        try {
            AnnotationRes annotationRes = annotationService.createAnnotation(annotationCreateReq);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, annotationRes);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(ErrorMessage.ERROR, HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping
    public ResponseEntity<Object> putAnnotation(@RequestBody @Valid AnnotationUpdateReq annotationUpdateReq) {

        try {
            AnnotationRes annotationRes = annotationService.updateAnnotation(annotationUpdateReq);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, annotationRes);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAnnotation(@RequestParam("id") Long id) {
        if (id == null) {
            return ResponseHandler.generateResponse(ErrorMessage.NO_PARAM, HttpStatus.BAD_REQUEST, null);
        }
        try {
            annotationService.deleteAnnotation(id);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NO_CONTENT, null);
        }
    }
}
