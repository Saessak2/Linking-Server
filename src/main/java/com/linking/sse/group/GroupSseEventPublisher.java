package com.linking.sse.group;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupSseEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishPostPageEvent() {

    }

}
