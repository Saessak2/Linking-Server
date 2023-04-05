package com.linking.page.dto;

import com.linking.annotation.dto.AnnotationRes;
import com.linking.block.dto.BlockRes;
import com.linking.pageCheck.dto.PageCheckRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRes {

    private Long pageId;
    private Long groupId;
    private String title;
    private int order;
    private List<BlockRes> blockResList;
    private List<PageCheckRes> pageCheckResList;

}
