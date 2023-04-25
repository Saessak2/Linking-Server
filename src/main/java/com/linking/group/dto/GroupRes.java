package com.linking.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "그룹 응답 DTO")
public class GroupRes {

    @Schema(description = "group id")
    private Long groupId;

    @Schema(description = "project id")
    private Long projectId;

    @Schema(description = "그룹 이름")
    private String name;
}
