package com.linking.page.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "페이지 제목 수정 요청 DTO")
public class PageUpdateTitleReq {

    @NotNull
    @Schema(description = "page id")
    private Long pageId;

    @NotNull
    @Schema(description = "페이지 제목")
    private String title;
}
