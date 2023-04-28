package com.linking.page.dto;

import com.linking.pageCheck.dto.PageCheckRes;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class PageDetailedRes {

    private Long pageId;
    private Long groupId;
    private String title;
    private List<PageCheckRes> pageCheckResList;

    public PageDetailedRes(Long pageId, Long groupId, String title, List<PageCheckRes> pageCheckResList) {
        this.pageId = pageId;
        this.groupId = groupId;
        this.title = title;
        this.pageCheckResList = pageCheckResList;
    }
}
