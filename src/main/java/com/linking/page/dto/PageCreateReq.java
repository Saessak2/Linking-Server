package com.linking.page.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageCreateReq {

    private Long projectId; // 프로젝트 id
    private Long parentDocId;
    private String title;
    private int docIndex;
}
