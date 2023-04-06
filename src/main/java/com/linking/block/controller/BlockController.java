package com.linking.block.controller;

import com.linking.block.dto.BlockCreateReq;
import com.linking.block.dto.BlockRes;
import com.linking.block.service.BlockService;
import com.linking.global.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/blocks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST})
public class BlockController {

    private final BlockService blockService;

    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<Object> postBlock(@RequestBody @Valid BlockCreateReq req) {
        try {
            BlockRes blockRes = blockService.createBlock(req);
            if (blockRes == null)
                return ResponseHandler.generateInternalServerErrorResponse();
            return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, blockRes);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }
}
