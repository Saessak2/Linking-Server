package com.linking.sse.notification.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSseHandler {

    private static final Long TIMEOUT = 600 * 1000L;


}
