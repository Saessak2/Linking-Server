package com.linking.annotation.controller;

import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateReq;
import com.linking.annotation.service.AnnotationService;
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
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class AnnotationController {

    private final AnnotationService annotationService;

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<Object> getAnnotations(@PathVariable("id") Long blockId) {

        return ResponseHandler.generateOkResponse(annotationService.findAnnotations(blockId));
    }


    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<Object> postAnnotation(@RequestBody @Valid AnnotationCreateReq req) {
        try {
            return annotationService.createAnnotation(req)
                    .map(ResponseHandler::generateCreatedResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            return ResponseHandler.generateBadRequestResponse();
        }
    }

    @PutMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.PUT})
    public ResponseEntity<Object> putAnnotation(@RequestBody @Valid AnnotationUpdateReq req) {

        try {
            AnnotationRes annotationRes = annotationService.updateAnnotation(req);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, annotationRes);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.DELETE})
    public ResponseEntity<Object> deleteAnnotation(@PathVariable("id") Long id) {
        try {
            annotationService.deleteAnnotation(id);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }
}
