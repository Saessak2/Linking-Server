package com.linking.page.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "페이지 id DTO")
public class PageOrderReq {

    @NotNull
    @Schema(description = "page id")
    private Long pageId;
}
