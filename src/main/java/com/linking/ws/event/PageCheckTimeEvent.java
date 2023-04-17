package com.linking.ws.event;

import com.linking.pageCheck.dto.PageCheckRes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PageCheckTimeEvent {

    private int resType;
    private Long userId;
    private Long pageId;
    private List<PageCheckRes> pageCheckResList;

    @Builder
    public PageCheckTimeEvent(int resType, Long userId, Long pageId, List<PageCheckRes> pageCheckResList) {
        this.resType = resType;
        this.userId = userId;
        this.pageId = pageId;
        this.pageCheckResList = pageCheckResList;
    }
}
