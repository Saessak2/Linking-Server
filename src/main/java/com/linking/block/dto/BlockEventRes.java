package com.linking.block.dto;

import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockEventRes {


    private Long blockId;
    private Long pageId;
    private String title;
    private String content;

    @Builder
    public BlockEventRes(Long blockId, Long pageId, String title, String content) {
        this.blockId = blockId;
        this.pageId = pageId;
        this.title = title;
        this.content = content;
    }
}
