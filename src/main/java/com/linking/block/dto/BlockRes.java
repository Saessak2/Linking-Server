package com.linking.block.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BlockRes {

    private Long blockId;
    private Long pageId;
    private String title;
    private String content;
}
