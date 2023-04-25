package com.linking.page.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "페이지 응답 DTO")
public class  PageRes {

    @Schema(description = "page id")
    private Long pageId;

    @Schema(description = "group id")
    private Long groupId;

    @Schema(description = "페이지 제목")
    private String title;

    @Schema(description = "페이지 순서 (index)")
    private int order;

    @Schema(description = "주석 알림 개수")
    private int annoNotCnt;
}
