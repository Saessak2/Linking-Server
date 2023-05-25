package com.linking.block.dto;

import lombok.*;


@Getter
public class BlockEventRes {


    private Long blockId;
    private Long pageId;
    private String title;

    public BlockEventRes(Long blockId, Long pageId, String title) {
        this.blockId = blockId;
        this.pageId = pageId;
        this.title = title;
    }
}
