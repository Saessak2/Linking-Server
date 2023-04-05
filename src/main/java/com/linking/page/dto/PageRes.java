package com.linking.page.dto;

import com.linking.document.dto.DocumentRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRes {

    private Long docId;
    private Long projectId;
    private Long parentDocId;
    private String title;
    private String createdDatetime;
    private String updatedDatetime;
//    private List<BlockRes> blockResList;
//    private List<PageCheckRes> pageCheckResList;

}
