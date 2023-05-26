package com.linking.block.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockCloneReq {

    private String cloneType;
    private String title;
    private String content;
    private Long pageId;

}
