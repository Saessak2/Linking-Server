package com.linking.page.dto;

import com.linking.global.util.DiffStr;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextOutputMessage {

    private Long pageId;
    private Long blockId; // blockId or -1
    private Integer editorType; // 0, 1, 2
    private DiffStr diffStr;
}
