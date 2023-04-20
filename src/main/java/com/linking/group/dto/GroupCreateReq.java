package com.linking.group.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "그룹 생성 요청 DTO")
public class GroupCreateReq {

    @NotNull
    @Schema(description = "프로젝트 pk")
    private Long projectId;

    @NotNull
    @Schema(description = "그룹 이름")
    private String name;

    @NotNull
    @Schema(description = "그룹 순서 (index)")
    private int order;
}
