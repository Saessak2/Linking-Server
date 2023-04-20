package com.linking.annotation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "주석 응답 DTO")
public class AnnotationRes {

    @Schema(description = "annotation id")
    private Long annotationId;

    @Schema(description = "block id")
    private Long blockId;

    @Schema(description = "주석 내용", minLength = 1, maxLength = 255)
    private String content;

    @Schema(description = "마지막 수정 시간", example = "2023-01-01 AM 01:01")
    private String lastModified;

    @Schema(description = "사용자 full name (성+이름)", example = "이은빈", minLength = 1, maxLength = 40)
    private String userName;
}
