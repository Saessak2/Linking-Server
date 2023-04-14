package com.linking.page.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRes {

    private Long pageId;
    private Long groupId;
    private String title;
    private int order;
    private int annotNotiCnt;
}
