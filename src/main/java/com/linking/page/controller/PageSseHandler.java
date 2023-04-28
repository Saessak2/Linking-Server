package com.linking.page.controller;

import com.linking.global.common.CustomEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PageSseHandler {

    private static final Long TIMEOUT = 600 * 1000L;

    /**
     * key : pageId
     */
    private final Map<Long, Set<CustomEmitter>> pageSubscriber = new ConcurrentHashMap<>();

    public SseEmitter connect(Long key, Long userId) {
        CustomEmitter customEmitter = new CustomEmitter(userId, new SseEmitter(TIMEOUT));

        log.info("@@ [PAGE][CONNECT] @@ key = {}", key);
        Set<CustomEmitter> sseEmitters = this.pageSubscriber.get(key);

        if (sseEmitters == null) {
            sseEmitters = Collections.synchronizedSet(new HashSet<>());
            sseEmitters.add(customEmitter);
            this.pageSubscriber.put(key, sseEmitters);

        } else {
            sseEmitters.add(customEmitter);
        }
        log.info("@@ [PAGE][EMITTERS] @@ emitters.size = {}", pageSubscriber.size());
        log.info("@@ [PAGE][EMIT_BY_PAGE] @@ key = {} @@ emitters.size = {}", key, sseEmitters.size());

        SseEmitter emitter = customEmitter.getSseEmitter();
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            remove(key, customEmitter);
        });
        return emitter;
    }

    public Set<Long> getUserIdsByPage(Long pageId) {
        Set<CustomEmitter> emitters = pageSubscriber.get(pageId);
        if (emitters == null)
            return new HashSet<>();
        return emitters.stream().map(CustomEmitter::getUserId).collect(Collectors.toSet());
    }

    public void remove(Long key, CustomEmitter emitter) {
        Set<CustomEmitter> sseEmitters = this.pageSubscriber.get(key);
        sseEmitters.remove(emitter);
        log.info("@@ [PAGE][EMIT_BY_PAGE] @@ pageId = {} @@ emitters.size = {}", key, sseEmitters.size());
    }

    public void send(Long key, Long publishUserId, String event, Object message) {
        Set<CustomEmitter> sseEmitters = this.pageSubscriber.get(key);
        if (sseEmitters == null) return;
        sseEmitters.forEach(emitter -> {
            if (publishUserId != emitter.getUserId()) {
                try {
                    emitter.getSseEmitter().send(SseEmitter.event()
                            .name(event)
                            .data(message));

                } catch (IOException e) {
                    log.error("emitter send exception");
                }
            }
        });
    }
}
