package com.linking.sse.ssehandler;

import com.linking.sse.domain.CustomEmitter;
import com.linking.sse.event.PageEvent;
import com.linking.sse.persistence.IEmitterRepository;
import com.linking.sse.persistence.PageEmitterInMemoryRepoImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PageSseHandler {

    private static final Long TIMEOUT = 600 * 1000L;

    private final PageEmitterInMemoryRepoImpl emitterRepository;

    public SseEmitter connect(Long pageId, Long userId) {

        log.info("[PAGE][CONNECT] pageId = {}, userId = {}", pageId, userId);

        CustomEmitter customEmitter = emitterRepository.save(pageId, new CustomEmitter(userId, new SseEmitter(TIMEOUT)));
        SseEmitter emitter = customEmitter.getSseEmitter();

        emitter.onTimeout(() -> {
            emitter.complete();
        });
        emitter.onCompletion(() -> {
            emitterRepository.deleteEmitter(pageId, customEmitter);
            log.info("[PAGE][REMOVE] pageId = {}, userId = {}", pageId, userId);
        });
        return emitter;
    }

    public Set<Long> enteringUserIds(Long pageId) {
        Set<CustomEmitter> emitters = emitterRepository.findEmittersByKey(pageId);
        if (emitters == null)
            return new HashSet<>();
        return emitters.stream().map(CustomEmitter::getUserId).collect(Collectors.toSet());
    }

    @EventListener
    public void send(PageEvent event) {

        Set<CustomEmitter> emittersByPage = emitterRepository.findEmittersByKey(event.getPageId());
        if (emittersByPage == null) return;

        sendEventExceptPublisher(event, emittersByPage);
    }

    private void sendEventExceptPublisher(PageEvent event, Set<CustomEmitter> emitters) {

        emitters.forEach(emitter -> {
            if (event.getUserId() != emitter.getUserId()) {
                try {
                    emitter.getSseEmitter().send(
                            SseEmitter.event()
                                    .name(event.getEventName())
                                    .data(event.getData()));
                    log.info("send {} event to user {}", event.getEventName(), emitter.getUserId());

                } catch (IOException e) {
                    log.error("Connection reset by peer");
                }

            }
        });
    }

    public void onClose(Long userId, Long pageId) {

        Set<CustomEmitter> customEmitters = emitterRepository.findEmittersByKey(pageId);
        if (customEmitters == null) return;
        for (CustomEmitter ce : customEmitters) {
            if (ce.getUserId() == userId) {
                ce.getSseEmitter().complete();
                break;
            }
        }
    }

    @Async("eventCallExecutor")
    public void removeEmittersByPage(Long pageId) { // 해당 페이지 emitters 삭제

        log.info("PageSseHandler.removeEmittersByPage");

        Set<CustomEmitter> customEmitters = emitterRepository.deleteAllByKey(pageId);

        if (customEmitters != null) {
            for (CustomEmitter ce : customEmitters) {
                if (ce.getSseEmitter() != null)
                    ce.getSseEmitter().complete();
            }
        }

        log.info("[PAGE][REMOVE_ALL] page = {} is removed", pageId);
    }
}
