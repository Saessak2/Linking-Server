package com.linking.page.dto;

import com.linking.block.dto.BlockRes;
import com.linking.pageCheck.dto.PageCheckRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "페이지 상세 조회 응답 DTO")
public class PageDetailedRes {

    @Schema(description = "page id")
    private Long pageId;

    @Schema(description = "group id")
    private Long groupId;

    @Schema(description = "페이지 제목")
    private String title;

    @Schema(description = "블럭 응답 리스트")
    private List<BlockRes> blockResList;

    @Schema(description = "페이지 확인 리스트")
    private List<PageCheckRes> pageCheckResList;
}
