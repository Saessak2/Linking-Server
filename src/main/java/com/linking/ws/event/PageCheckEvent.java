package com.linking.ws.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageCheckEvent<T> {
    private int resType;
    private Long pageId;
    private T data;

    @Builder
    public PageCheckEvent(int resType, Long pageId, T data) {
        this.resType = resType;
        this.pageId = pageId;
        this.data = data;
    }
}
