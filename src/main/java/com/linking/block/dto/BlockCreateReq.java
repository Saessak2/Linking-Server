package com.linking.block.dto;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class BlockCreateReq {

    private int blockIndex;
    private Long pageId;
}
