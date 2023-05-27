package com.linking.page.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextSendEvent {

    private String sessionId;
    private Long pageId;
    private TextOutputMessage textOutputMessage;
}
