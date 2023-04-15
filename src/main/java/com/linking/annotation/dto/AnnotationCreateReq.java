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
    private Long projectId;
    @NotNull
    private Long blockId;
    @NotNull
    private Long userId;
    @NotNull
    private String userName;

}
