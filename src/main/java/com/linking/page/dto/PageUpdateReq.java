package com.linking.page.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageUpdateReq {

    private Long pageId;
    private Long parentDocId;
    private String title;
    private int docIndex;
}
