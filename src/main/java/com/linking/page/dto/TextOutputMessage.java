package com.linking.page.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextOutputMessage {

    private Long pageId;
    private Long blockId; // blockId or -1
    private Integer editorType; // 0, 1, 2
    private String docs;
}
