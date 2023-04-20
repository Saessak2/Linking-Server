package com.linking.group.dto;

import com.linking.page.dto.PageRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "그룹 응답 DTO")
public class GroupRes{

    @Schema(description = "group id")
    private Long groupId;

    @Schema(description = "project id")
    private Long projectId;

    @Schema(description = "그룹 이름")
    private String name;

    @Schema(description = "그룹 순서 (index)")
    private int order;

    @Schema(description = "page 응답 리스트")
    private List<PageRes> pageResList;
}
