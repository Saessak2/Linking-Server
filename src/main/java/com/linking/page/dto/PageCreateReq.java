package com.linking.page.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "페이지 생성 요청 DTO")
public class PageCreateReq {

    @NotNull
    @Schema(description = "group id")
    private Long groupId;

    @NotNull
    @Schema(description = "페이지 제목")
    private String title;

    @NotNull
    @Schema(description = "페이지 순서 (index)")
    private int order;
}
