package com.linking.page.dto;

import lombok.Getter;

@Getter
public class PageIdRes {

    private Long groupId;
    private Long pageId;

    public PageIdRes(Long groupId, Long pageId) {
        this.groupId = groupId;
        this.pageId = pageId;
    }
}
