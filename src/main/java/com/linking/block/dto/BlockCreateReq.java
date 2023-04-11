package com.linking.block.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BlockCreateReq {

    @NotNull
    private int order;
    @NotNull
    private Long pageId;

    private String title;
}
