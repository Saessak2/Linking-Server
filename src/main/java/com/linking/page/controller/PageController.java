package com.linking.page.controller;

import com.linking.global.ErrorMessage;
import com.linking.global.ResponseHandler;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageRes;
import com.linking.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/pages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE})
public class PageController {

    private final PageService pageService;

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<Object> getPage(@PathVariable("id") Long pageId) {
        try {
            PageRes pageRes = pageService.getPage(pageId);
            if (pageRes == null)
                return ResponseHandler.generateInternalServerErrorResponse();
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, pageRes);
        } catch (NoSuchElementException e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<Object> postPage(@RequestBody @Valid PageCreateReq pageCreateReq) {
        try {
            PageRes pageRes = pageService.createPage(pageCreateReq);
            if (pageRes == null)
                return ResponseHandler.generateInternalServerErrorResponse();
            return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, pageRes);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

//    @PutMapping
//    public ResponseEntity<Object> putPage(@RequestBody @Valid PageUpdateReq pageUpdateReq) {
//        try {
//            PageRes pageRes = pageService.updatePage(pageUpdateReq);
//            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, pageRes);
//        } catch (NoSuchElementException e) {
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
//        }
//    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.DELETE})
    public ResponseEntity<Object> deletePage(@PathVariable("id") Long docId) {
        try {
            pageService.deletePage(docId);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

}

