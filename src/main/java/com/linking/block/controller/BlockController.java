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
@Tag(name = "블")
public class BlockController {

    private final BlockService blockService;

    @PostMapping
    @Operation(summary = "블록 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = BlockRes.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    public ResponseEntity<Object> postBlock(@RequestBody @Valid BlockCreateReq req) {
        BlockRes blockRes = blockService.createBlock(req);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, blockRes);
    }

    @PutMapping("/order")
    @Operation(summary = "블록 순서 변경", description = "정상 처리 된 경우 Body-data에 True를 반환함")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    public ResponseEntity<Object> putBlockOrder(@RequestBody @Valid List<BlockOrderReq> req) {
        blockService.updateBlockOrder(req);
        return ResponseHandler.generateOkResponse(true);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBlock(@Parameter(description = "블록 id", in = ParameterIn.PATH) @PathVariable("id") Long blockId) {
        blockService.deleteBlock(blockId);
        return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
    }
}
