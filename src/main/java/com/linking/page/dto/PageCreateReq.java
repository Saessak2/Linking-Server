package com.linking.page.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageCreateReq {

    @NotNull
    private Long projectId; // 프로젝트 id
    @NotNull
    private Long parentDocId;
    @NotNull
    private String title;
    @NotNull
    private int docIndex;
}
