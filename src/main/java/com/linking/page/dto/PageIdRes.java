package com.linking.page.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PageIdRes {

    private Long gropupId;
    private Long pageId;

    public PageIdRes(Long gropupId, Long pageId) {
        this.gropupId = gropupId;
        this.pageId = pageId;
    }
}
