package com.linking.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "그룹 이름 변경 요청 DTO")
public class GroupNameReq {

    @NotNull
    @Schema(description = "group id")
    private Long groupId;

    @NotNull
    @Schema(description = "그룸 이름")
    private String name;
}
