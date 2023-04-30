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
import java.util.List;

@RestController
@RequestMapping("/blocks")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @PostMapping
    public ResponseEntity<Object> postBlock(
            @RequestHeader(value = "userId") Long userId, @RequestBody @Valid BlockCreateReq req
    ) {

        BlockRes blockRes = blockService.createBlock(req, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, blockRes.getBlockId());
    }

    @PutMapping("/order")
    public ResponseEntity<Object> putBlockOrder(
            @RequestHeader(value = "userId") Long userId,
            @RequestBody @Valid BlockOrderReq req
    ) {

        blockService.updateBlockOrder(req, userId);
        return ResponseHandler.generateOkResponse(true);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBlock(
            @RequestHeader(value = "userId") Long userId,
            @PathVariable("id") Long blockId
    ) {

        blockService.deleteBlock(blockId, userId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
    }
}
