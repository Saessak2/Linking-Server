package com.linking.domain.block.controller;

import com.linking.domain.block.dto.BlockCreateReq;
import com.linking.domain.block.dto.BlockOrderReq;
import com.linking.domain.block.dto.BlockRes;
import com.linking.domain.block.service.BlockService;
import com.linking.global.common.Login;
import com.linking.global.common.ResponseHandler;
import com.linking.global.common.UserCheck;
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
            @Login UserCheck userCheck
    ) {

        BlockRes blockRes = blockService.createBlock(req, userCheck.getUserId());
        return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, blockRes.getBlockId());
    }

    @PutMapping("/order")
    public ResponseEntity<Object> putBlockOrder(
            @RequestBody @Valid BlockOrderReq req,
            @Login UserCheck userCheck
    ) {

        boolean res = blockService.updateBlockOrder(req, userCheck.getUserId());
        return ResponseHandler.generateOkResponse(res);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBlock(
            @PathVariable("id") Long blockId,
            @Login UserCheck userCheck
    ) {

        blockService.deleteBlock(blockId, userCheck.getUserId());
        return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
    }
}
