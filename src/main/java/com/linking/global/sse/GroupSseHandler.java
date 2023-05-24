package com.linking.global.sse;

import com.linking.global.common.CustomEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
        log.info("[GROUP][CONNECT] userId = {}, projectId = {}", userId, key);

        Set<CustomEmitter> customEmitters = this.addEmitter(key, customEmitter);
        SseEmitter emitter = customEmitter.getSseEmitter();

        emitter.onTimeout(() -> {
            emitter.complete();
        });

        emitter.onCompletion(()-> {
            customEmitters.remove(customEmitter);
            log.info("[GROUP][REMOVE] projectId = {} @@ emitters.size = {}", key, customEmitters.size());
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
        log.info("[GROUP][ADD] projectId = {}, size = {}", key, sseEmitters.size());

        return sseEmitters;
    }

    @EventListener
    public void send(GroupEvent event) {

        Set<CustomEmitter> emittersByProject = this.groupSubscriber.get(event.getProjectId());
        if (emittersByProject == null) return;

        if (event.getUserId() == null)
            sendEventExceptUserIdList(event, emittersByProject);
        else
            sendEventExceptUserId(event, emittersByProject);

    }

    public void sendEventExceptUserId(GroupEvent event, Set<CustomEmitter> emitters) {

        emitters.forEach(emitter -> {
            if (event.getUserId() != emitter.getUserId()) {
                try {
                    emitter.getSseEmitter().send(
                            SseEmitter.event()
                                    .name(event.getEventName())
                                    .data(event.getData()));
                    log.info("send {} event", event.getEventName());

                } catch (IOException e) {
                    log.error("Connection reset by peer");
                }

            }
        });

    }

    public void sendEventExceptUserIdList(GroupEvent event, Set<CustomEmitter> emitters) {

        emitters.forEach(emitter -> {
            if (!event.getUserIds().contains(emitter.getUserId())) {
                try {
                    emitter.getSseEmitter().send(
                            SseEmitter.event()
                                    .name(event.getEventName())
                                    .data(event.getData()));
                    log.info("send {} event", event.getEventName());

                } catch (IOException e) {
                    log.error("Connection reset by peer");
                }
            }
        });
    }


//
//    public void send(Long key, Long publishUserId, String event, Object message) {
//
//        Set<CustomEmitter> sseEmitters = this.groupSubscriber.get(key);
//        if (sseEmitters == null) return;
//
//        sseEmitters.forEach(emitter -> {
//            if (publishUserId != emitter.getUserId()) {
//                try {
//                    emitter.getSseEmitter().send(SseEmitter.event()
//                            .name(event)
//                            .data(message));
//                    log.info("send {} event", event);
//
//                } catch (IOException e) {
//                    log.error("Connection reset by peer");
//                }
//            }
//        });
//    }

//
//    /**
//     * send for event of post/deleteAnnoNot
//     */
//    public void send(Long key, Set<Long> pageSubscriberIds, String event, Object message) {
//
//        Set<CustomEmitter> sseEmitters = this.groupSubscriber.get(key);
//        if (sseEmitters == null) return;
//        sseEmitters.forEach(emitter -> {
//            if (!pageSubscriberIds.contains(emitter.getUserId())) {
//                try {
//                    emitter.getSseEmitter().send(SseEmitter.event()
//                            .name(event)
//                            .data(message));
//                    log.info("send {} event", event);
//
//                } catch (IOException e) {
//                    log.error("Connection reset by peer");
//                }
//            }
//        });
//    }

    @Async("eventCallExecutor")
    public void removeEmittersByProject(Long key) {
        log.info("removeEmittersByProject - {}", this.getClass().getName());

        Set<CustomEmitter> customEmitters = groupSubscriber.get(key);
        for (CustomEmitter customEmitter : customEmitters) {
            if (customEmitter.getSseEmitter() != null)
                customEmitter.getSseEmitter().complete();
        }

        groupSubscriber.remove(key);
        log.info("** [GROUP][REMOVE_ALL] project = {} is removed", key);
    }
}
