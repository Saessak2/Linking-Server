package com.linking.page.dto;

import com.linking.page.domain.Template;
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
    private Long groupId;

    @NotNull
    private String title;

    @NotNull
    private int order;

    @NotNull
    private Template template;
}
