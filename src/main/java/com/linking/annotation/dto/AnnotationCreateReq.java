package com.linking.annotation.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "주석 생성 요청 DTO")
public class AnnotationCreateReq {

    @NotNull
    @Size(min = 1, max = 255)
    @Schema(description = "내용", minLength = 1, maxLength = 255)
    private String content;

    @NotNull
    @Schema(description = "project id")
    private Long projectId;

    @NotNull
    @Schema(description = "block id")
    private Long blockId;

    @NotNull
    @Schema(description = "user id")
    private Long userId;
}
