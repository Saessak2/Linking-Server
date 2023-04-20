package com.linking.block.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "블럭 순서 변경 - block id DTO")
public class BlockOrderReq {

    @Schema(description = "block id")
    private Long blockId;
}
