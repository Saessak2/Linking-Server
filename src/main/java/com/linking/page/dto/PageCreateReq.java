package com.linking.page.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageCreateReq {

    @NotNull
    private Long groupId;
    @NotNull
    private String title;
    @NotNull
    private int order;
}
