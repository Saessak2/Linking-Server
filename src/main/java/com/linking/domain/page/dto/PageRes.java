package com.linking.domain.page.dto;

import com.linking.domain.page.domain.Template;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  PageRes {

    private Long pageId;
    private Long groupId;
    private String title;
    private Template template;
    private int annoNotCnt;
}
