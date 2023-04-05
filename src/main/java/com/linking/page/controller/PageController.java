package com.linking.page.controller;

import com.linking.global.ErrorMessage;
import com.linking.global.ResponseHandler;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageRes;
import com.linking.page.dto.PageUpdateReq;
import com.linking.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/pages")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @PostMapping
    public ResponseEntity<Object> postPage(@RequestBody PageCreateReq pageCreateReq) {

        try {
            PageRes pageRes = pageService.createPage(pageCreateReq);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, pageRes);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(ErrorMessage.ERROR, HttpStatus.BAD_REQUEST, null);
        }
    }

//    @PutMapping
//    public ResponseEntity<Object> putPage(@RequestBody PageUpdateReq pageUpdateReq) {
//        try {
//            PageRes pageRes = pageService.updatePage(pageUpdateReq);
//            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, pageRes);
//        } catch (NoSuchElementException e) {
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
//        }
//    }

    @DeleteMapping
    public ResponseEntity<Object> deletePage(@RequestParam("id") Long docId) {
        try {
            pageService.deletePage(docId);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }
}

