package com.linking.group.controller;

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
public class SseEmitters {

    private final Map<Long, Set<SseEmitter>> CLIENTS = new ConcurrentHashMap<>();

    SseEmitter add(Long key, SseEmitter emitter) {

        Set<SseEmitter> sseEmitters = this.CLIENTS.get(key);
        if (sseEmitters == null) {
            sseEmitters = Collections.synchronizedSet(new HashSet<>());
            sseEmitters.add(emitter);
            this.CLIENTS.put(key, sseEmitters);

        } else {
            sseEmitters.add(emitter);
        }
        log.info("@@ [DOC][EMITTERS] @@ emitters.size = {}", CLIENTS.size());
        log.info("@@ [DOC][EMIT_BY_PROJECT] @@ key = {} @@ emitters.size = {}", key, sseEmitters.size());

        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        emitter.onCompletion(()-> {
            log.info("onCompletion callback");
            remove(key, emitter);
        });
        return emitter;
    }

    void remove(Long key, SseEmitter emitter) {
        Set<SseEmitter> sseEmitters = this.CLIENTS.get(key);
        sseEmitters.remove(emitter);
        log.info("@@ [DOC][EMIT_BY_PROJECT] @@ projectId = {} @@ emitters.size = {}", key, sseEmitters.size());
    }

    void send(Long key, String event, Object message) {
        Set<SseEmitter> sseEmitters = this.CLIENTS.get(key);
        sseEmitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(event)
                        .data(message));

            } catch (IOException e) {
                log.error("emitter send exception");
            }
        });
    }
}
