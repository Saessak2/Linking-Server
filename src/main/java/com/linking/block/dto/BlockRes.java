package com.linking.block.dto;

import com.linking.annotation.dto.AnnotationRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "블럭 응답 DTO")
public class BlockRes {

    @Schema(description = "block id")
    private Long blockId;

    @Schema(description = "블럭 제목")
    private String title;

    @Schema(description = "블럭 내용")
    private String content;

    @Schema(description = "블럭 순서 (index)")
    private int order;

    @Schema(description = "page id")
    private Long pageId;

    @Schema(description = "주석 응답 리스트")
    private List<AnnotationRes> annotationResList;
}
