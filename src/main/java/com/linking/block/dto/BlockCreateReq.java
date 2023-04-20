package com.linking.block.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "블럭 생성 DTO")
public class BlockCreateReq {

    @NotNull
    @Schema(description = "블럭 순서 (index)")
    private int order;

    @NotNull
    @Schema(description = "page id")
    private Long pageId;

    @Schema(description = "블럭 title")
    private String title;
}
