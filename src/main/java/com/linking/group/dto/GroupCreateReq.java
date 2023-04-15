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
    private String name;
    @NotNull
    private int order;
}
