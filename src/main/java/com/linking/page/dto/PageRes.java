package com.linking.page.dto;

import com.linking.block.dto.BlockRes;
import com.linking.document.dto.DocumentRes;
import com.linking.pageCheck.dto.PageCheckRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRes extends DocumentRes {

    private Long pageId;
    private Long projectId;
    private Long parentDocId;
    private String title;
    private String createdDatetime;
    private String updatedDatetime;
//    private List<BlockRes> blockResList;
//    private List<PageCheckRes> pageCheckResList;

}
