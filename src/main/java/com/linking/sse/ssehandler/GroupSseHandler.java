package com.linking.sse.ssehandler;

import com.linking.global.common.CustomEmitter;
import com.linking.sse.event.GroupEvent;
import com.linking.sse.emitter_repository.IGroupSseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupSseHandler {

    private static final Long TIMEOUT = 600 * 1000L; // 10분
    private final IGroupSseRepository groupSseInMemoryRepository;

    public SseEmitter connect(Long projectId, Long userId) {

        log.info("[GROUP][CONNECT] projectId = {}, userId = {}", projectId, userId);

        CustomEmitter customEmitter = groupSseInMemoryRepository.save(projectId, new CustomEmitter(userId, new SseEmitter(TIMEOUT)));
        SseEmitter emitter = customEmitter.getSseEmitter();

        emitter.onTimeout(() -> {
            emitter.complete();
        });
        emitter.onCompletion(() -> {
            groupSseInMemoryRepository.deleteEmitter(projectId, customEmitter);
            log.info("[GROUP][REMOVE] projectId = {}, userId = {}", projectId, userId);
        });
        return emitter;
    }

    @EventListener
    public void send(GroupEvent event) {

        Set<CustomEmitter> emittersByProject = groupSseInMemoryRepository.findEmittersByKey(event.getProjectId());
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
                    log.info("send {} event to user {}", event.getEventName(), emitter.getUserId());

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

    @Async("eventCallExecutor")
    public void removeEmittersByProject(Long projectId) {

        log.info("GroupSseHandler.removeEmittersByProject");

        Set<CustomEmitter> customEmitters = groupSseInMemoryRepository.deleteAllByProject(projectId);

        if (customEmitters != null) {
            for (CustomEmitter customEmitter : customEmitters) {
                if (customEmitter.getSseEmitter() != null)
                    customEmitter.getSseEmitter().complete();
            }
        }

        log.info("** [GROUP][REMOVE_ALL] project = {} is removed", projectId);
    }
}
