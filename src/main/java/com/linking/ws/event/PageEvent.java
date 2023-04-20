package com.linking.ws.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageEvent<T> {

    private int resType;
    private Long userId;
    private Long pageId;
    private T data;

    @Builder
    public PageEvent(int resType, Long userId, Long pageId, T data) {
        this.resType = resType;
        this.userId = userId;
        this.pageId = pageId;
        this.data = data;
    }
}
