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

    private Long annotationId;
    private Long blockId;
    private String content;
    @Schema(description = "마지막 수정 시간", example = "2023-01-01 AM 01:01")
    private String lastModified;
    private String userName;
}
