package com.linking.annotation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주석 내용 수정 요청 DTO")
public class AnnotationUpdateReq {

    @NotNull
    private Long annotationId;
    @NotNull
    @Size(min = 1, max = 255)
    @Schema(description = "주석 내용", minLength = 1, maxLength = 255)
    private String content;
}
