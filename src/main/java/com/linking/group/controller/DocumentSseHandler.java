package com.linking.group.controller;

import com.linking.global.common.CustomEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DocumentSseHandler {

    private static final Long TIMEOUT = 600 * 1000L;
    /**
     * key : (Long) projectId
     */
    private final Map<Long, Set<CustomEmitter>> documentSubscriber = new ConcurrentHashMap<>();

    public SseEmitter connect(Long key, Long userId) {
        CustomEmitter customEmitter = new CustomEmitter(userId, new SseEmitter(TIMEOUT));
        log.info("@@ [DOC][CONNECT] @@ key = {}", key);
        Set<CustomEmitter> sseEmitters = this.documentSubscriber.get(key);

        if (sseEmitters == null) {
            sseEmitters = Collections.synchronizedSet(new HashSet<>());
            sseEmitters.add(customEmitter);
            this.documentSubscriber.put(key, sseEmitters);

        } else {
            sseEmitters.add(customEmitter);
        }
        log.info("@@ [DOC][EMITTERS] @@ emitters.size = {}", documentSubscriber.size());
        log.info("@@ [DOC][EMIT_BY_PROJECT] @@ key = {} @@ emitters.size = {}", key, sseEmitters.size());

        SseEmitter emitter = customEmitter.getSseEmitter();
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        emitter.onCompletion(()-> {
            log.info("onCompletion callback");
            remove(key, customEmitter);
        });
        return emitter;
    }

    public void remove(Long key, CustomEmitter emitter) {
        Set<CustomEmitter> sseEmitters = this.documentSubscriber.get(key);
        sseEmitters.remove(emitter);
        log.info("@@ [DOC][EMIT_BY_PROJECT] @@ projectId = {} @@ emitters.size = {}", key, sseEmitters.size());
    }

    public void send(Long key, Long publishUserId, String event, Object message) {
        Set<CustomEmitter> sseEmitters = this.documentSubscriber.get(key);
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
