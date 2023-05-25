package com.linking.page.controller;

import com.linking.global.common.CustomEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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
        log.info("[PAGE][CONNECT] ** pageId = {}, userId = {}, emitter = {}", key, userId, customEmitter.getSseEmitter());

        Set<CustomEmitter> customEmitters = this.addEmitter(key, customEmitter);
        SseEmitter emitter = customEmitter.getSseEmitter();

        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            customEmitters.remove(customEmitter);
            log.info("** [PAGE][REMOVE_ONE] ** pageId = {} @@ emitters.size = {}", key, customEmitters.size());
        });
        return emitter;
    }

    public Set<CustomEmitter> addEmitter(Long key, CustomEmitter customEmitter) {
        Set<CustomEmitter> sseEmitters = this.pageSubscriber.get(key);

        if (sseEmitters == null) {
            sseEmitters = Collections.synchronizedSet(new HashSet<>());
            sseEmitters.add(customEmitter);
            this.pageSubscriber.put(key, sseEmitters);
        } else {
            sseEmitters.add(customEmitter);
        }
        log.info("** [PAGE][ALL_EMITTERS] ** emitters.size = {}", pageSubscriber.size());
        log.info("** [PAGE][ADD] @@ pageId = {} ** emitters.size = {}", key, sseEmitters.size());

        return sseEmitters;
    }

    public void send(Long key, Long publishUserId, String event, Object message) {
        log.info("async test" + Thread.currentThread());

        Set<CustomEmitter> sseEmitters = this.pageSubscriber.get(key);
        if (sseEmitters == null) return;
        sseEmitters.forEach(emitter -> {
            if (publishUserId != emitter.getUserId()) {
                try {
                    emitter.getSseEmitter().send(SseEmitter.event()
                            .name(event)
                            .data(message));
                    log.info("send {} event", event);
                } catch (IOException e) {
                    log.error("IOException > send {}", emitter.getSseEmitter());
                }
            }
        });
    }

    public void onClose(Long userId, Long pageId) {
        Set<CustomEmitter> customEmitters = this.pageSubscriber.get(pageId);
        if (customEmitters == null) return;
        for (CustomEmitter ce : customEmitters) {
            if (ce.getUserId() == userId) {
                ce.getSseEmitter().complete();
                break;
            }
        }
    }

    public Set<Long> enteringUserIds(Long pageId) {
        Set<CustomEmitter> emitters = pageSubscriber.get(pageId);
        if (emitters == null)
            return new HashSet<>();
        return emitters.stream().map(CustomEmitter::getUserId).collect(Collectors.toSet());
    }

    @Async("eventCallExecutor")
    public void removeEmittersByPage(Long key) { // 해당 페이지 emitters 삭제
        log.info("removeEmitterByPage - {}", this.getClass().getName());

        // pageSubscriber에서 remove(key)해도 emitter객체는 complete이 발생하기 전까지 삭제되지 않음.
        Set<CustomEmitter> customEmitters = pageSubscriber.get(key);
        for (CustomEmitter customEmitter : customEmitters) {
            if (customEmitter.getSseEmitter() != null)
                customEmitter.getSseEmitter().complete();
        }

        pageSubscriber.remove(key);
        log.info("** [PAGE][REMOVE_ALL] page = {} is removed", key);
    }
}
