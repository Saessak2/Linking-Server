package com.linking.block.controller;

import com.linking.block.dto.BlockCreateReq;
import com.linking.block.dto.BlockOrderReq;
import com.linking.block.dto.BlockRes;
import com.linking.block.service.BlockService;
import com.linking.global.common.ResponseHandler;
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
import java.util.List;

@RestController
@RequestMapping("/blocks")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @PostMapping
    public ResponseEntity<Object> postBlock(
            @RequestHeader(value = "userId") Long userId,
            @RequestBody @Valid BlockCreateReq req) {

        BlockRes blockRes = blockService.createBlock(req);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, blockRes);
    }

    @PutMapping("/order")
    public ResponseEntity<Object> putBlockOrder(@RequestBody @Valid List<BlockOrderReq> req) {

        blockService.updateBlockOrder(req);
        return ResponseHandler.generateOkResponse(true);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBlock(
            @PathVariable("id") Long blockId) {

        blockService.deleteBlock(blockId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
    }
}
