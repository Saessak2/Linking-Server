package com.linking.todo.controller;

import com.linking.global.common.LabeledEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class TodoSseHandler {

    private static final Long TIMEOUT = 600 * 1000L;
    private static int createdTodoEmitter = 0;
    private final List<LabeledEmitter> labeledEmitterList = new CopyOnWriteArrayList<>();

    public LabeledEmitter connect(Long userId){
        LabeledEmitter labeledEmitter = new LabeledEmitter(createdTodoEmitter++, userId, new SseEmitter(TIMEOUT));
        SseEmitter sseEmitter = addEmitter(labeledEmitter);
        log.info("[CONNECT] ** emitterId = {}, userId = {}, emitter = {}", createdTodoEmitter, userId, sseEmitter);

        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onCompletion(() -> {
            log.info("[REMOVE] ** userId = {}", labeledEmitter.getUserId());
            labeledEmitterList.remove(labeledEmitter);
        });

        return labeledEmitter;
    }

    public void disconnect(int emitterId) {
        for(LabeledEmitter labeledEmitter : labeledEmitterList)
            if(labeledEmitter.getEmitterId() == emitterId)
                labeledEmitter.getSseEmitter().complete();
    }

    public SseEmitter addEmitter(LabeledEmitter labeledEmitter){
        labeledEmitterList.add(labeledEmitter);
        SseEmitter sseEmitter = labeledEmitter.getSseEmitter();

        sseEmitter.onCompletion(() -> {
            labeledEmitterList.remove(labeledEmitter);
        });
        sseEmitter.onTimeout(sseEmitter::complete);

        return sseEmitter;
    }

    public void send(int emitterId, String eventName, Object data) {
        if(labeledEmitterList.isEmpty())
            return;
        for(LabeledEmitter labeledEmitter : labeledEmitterList){
            if(labeledEmitter.getEmitterId() != emitterId) {
                try {
                    labeledEmitter.getSseEmitter()
                            .send(SseEmitter.event().name(eventName).data(data));
                    log.info("SEND {} EVENT", eventName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
