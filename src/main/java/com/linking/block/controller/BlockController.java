package com.linking.block.controller;

import com.linking.block.dto.BlockCreateReq;
import com.linking.block.dto.BlockOrderReq;
import com.linking.block.dto.BlockRes;
import com.linking.block.service.BlockService;
import com.linking.global.common.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/blocks")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @PostMapping
    public ResponseEntity<Object> postBlock(
            @RequestBody @Valid BlockCreateReq req,
            @RequestHeader Long userId
    ) {

        BlockRes blockRes = blockService.createBlock(req, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, blockRes.getBlockId());
    }

    @PutMapping("/order")
    public ResponseEntity<Object> putBlockOrder(
            @RequestBody @Valid BlockOrderReq req,
            @RequestHeader Long userId
    ) {

        boolean res = blockService.updateBlockOrder(req, userId);
        return ResponseHandler.generateOkResponse(res);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBlock(
            @PathVariable("id") Long blockId,
            @RequestHeader Long userId
    ) {

        blockService.deleteBlock(blockId, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
    }
}
