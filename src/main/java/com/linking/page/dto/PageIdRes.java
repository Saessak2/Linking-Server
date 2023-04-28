package com.linking.page.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PageIdRes {

    private Long pageId;

    public PageIdRes(Long pageId) {
        this.pageId = pageId;
    }
}
