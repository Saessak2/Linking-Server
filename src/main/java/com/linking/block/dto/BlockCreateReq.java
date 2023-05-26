package com.linking.block.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BlockCreateReq {

    @Setter
    private int order;

    @NotNull
    private Long pageId;

    private String title;
}
