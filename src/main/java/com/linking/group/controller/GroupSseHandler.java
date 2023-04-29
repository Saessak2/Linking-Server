package com.linking.group.controller;

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
public class GroupSseHandler {

    private static final Long TIMEOUT = 600 * 1000L; // 10분
    /**
     * key : (Long) projectId
     */
    private final Map<Long, Set<CustomEmitter>> groupSubscriber = new ConcurrentHashMap<>();

    public SseEmitter connect(Long key, Long userId) {
        CustomEmitter customEmitter = new CustomEmitter(userId, new SseEmitter(TIMEOUT));
        log.info("[GROUP][CONNECT] userId = {}, projectId = {}, emitter = {}", userId, key, customEmitter.getSseEmitter());

        Set<CustomEmitter> customEmitters = this.addEmitter(key, customEmitter);
        SseEmitter emitter = customEmitter.getSseEmitter();

        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        emitter.onCompletion(()-> {
            log.info("onCompletion callback");
            customEmitters.remove(customEmitter);
            log.info("@@ [GROUP][REMOVE_ONE] @@ projectId = {} @@ emitters.size = {}", key, customEmitters.size());
        });
        return emitter;
    }

    public Set<CustomEmitter> addEmitter(Long key, CustomEmitter customEmitter) {
        Set<CustomEmitter> sseEmitters = this.groupSubscriber.get(key);

        if (sseEmitters == null) {
            sseEmitters = Collections.synchronizedSet(new HashSet<>());
            sseEmitters.add(customEmitter);
            this.groupSubscriber.put(key, sseEmitters);
        } else {
            sseEmitters.add(customEmitter);
        }
        log.info("@@ [GROUP][ALL_EMITTERS] @@ emitters.size = {}", groupSubscriber.size());
        log.info("@@ [GROUP][ADD] @@ key = {} @@ emitters.size = {}", key, sseEmitters.size());

        return sseEmitters;
    }

    public void send(Long key, Long publishUserId, String event, Object message) {
        Set<CustomEmitter> sseEmitters = this.groupSubscriber.get(key);
        if (sseEmitters == null) return;
        sseEmitters.forEach(emitter -> {
            if (publishUserId != emitter.getUserId()) {
                try {
                    emitter.getSseEmitter().send(SseEmitter.event()
                            .name(event)
                            .data(message));

                } catch (IOException e) {
                    log.error("Connection reset by peer");
                }
            }
        });
    }

    public void send(Long key, Set<Long> subscriberIds, String event, Object message) {
        Set<CustomEmitter> sseEmitters = this.groupSubscriber.get(key);
        if (sseEmitters == null) return;
        sseEmitters.forEach(emitter -> {
            if (subscriberIds.contains(emitter.getUserId())) {
                try {
                    emitter.getSseEmitter().send(SseEmitter.event()
                            .name(event)
                            .data(message));

                } catch (IOException e) {
                    log.error("Connection reset by peer");
                }
            }
        });
    }

    // TODO 프로젝트 삭제 했을 떄 publisher에서 project 삭제해야함.E
}
