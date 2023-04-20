package com.linking.group.dto;

import com.linking.page.dto.PageOrderReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "그룹 및 페이지 id (순서 변경 요청 DTO)")
public class GroupOrderReq {

    @NotNull
    @Schema(description = "group id")
    private Long groupId;

    @NotNull
    @Schema(description = "List<PageOrderReq>")
    private List<PageOrderReq> pageList;
}
