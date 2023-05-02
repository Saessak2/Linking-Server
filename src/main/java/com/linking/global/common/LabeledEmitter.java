package com.linking.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
@AllArgsConstructor
public class LabeledEmitter {

    private int emitterId;
    private Long userId;
    private SseEmitter sseEmitter;

}
