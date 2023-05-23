package com.linking.domain.page.dto;

import com.linking.domain.page.domain.Template;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Setter(AccessLevel.NONE)
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
