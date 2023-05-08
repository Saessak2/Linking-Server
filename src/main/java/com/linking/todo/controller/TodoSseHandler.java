package com.linking.todo.controller;

import com.linking.global.common.LabeledEmitter;
import com.linking.todo.dto.TodoSseConnectData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class TodoSseHandler {

    private static final Long TIMEOUT = 600 * 1000L;
    private static int createdTodoEmitter = 0;
    private final List<LabeledEmitter> labeledEmitterList = new CopyOnWriteArrayList<>();

    public LabeledEmitter connect(String clientType, Long userId) throws IOException {
        LabeledEmitter labeledEmitter = new LabeledEmitter(
                ++createdTodoEmitter, userId, clientType, new SseEmitter(TIMEOUT));
        SseEmitter sseEmitter = addEmitter(labeledEmitter);
        log.info("[CONNECT] ** emitterId = {}, userId = {}, clientType = {}, emitter = {}",
                labeledEmitter.getEmitterId(), labeledEmitter.getUserId(), labeledEmitter.getClientType(), sseEmitter);

        sseEmitter.send(SseEmitter.event().name("connect").data(new TodoSseConnectData(labeledEmitter.getEmitterId())));

        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onCompletion(() -> {
            log.info("[REMOVE] ** emitterId = {}, userId = {}", labeledEmitter.getEmitterId(), labeledEmitter.getUserId());
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

        sseEmitter.onCompletion(() -> labeledEmitterList.remove(labeledEmitter));
        sseEmitter.onTimeout(sseEmitter::complete);

        return sseEmitter;
    }

    public void send(int emitterId, String eventName, Object data) {
        log.info("send - {}", this.getClass().getSimpleName());

        if(labeledEmitterList.isEmpty())
            return;
        LabeledEmitter labeledEmitter =
                labeledEmitterList.stream().filter(e -> e.getEmitterId() == emitterId)
                        .findAny().orElseThrow(NoSuchElementException::new);

        if(labeledEmitter.getClientType().equals("web")) {
            sendToAllEmitters(emitterId, eventName, data);
        }
        else if(labeledEmitter.getClientType().equals("mac")) {
            sendToAllUsers(labeledEmitter.getUserId(), eventName, data);
        }
    }

    private void sendToAllEmitters(int emitterId, String eventName, Object data){
        for(LabeledEmitter labeledEmitter : labeledEmitterList) {
            if (labeledEmitter.getEmitterId() != emitterId) {
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

    private void sendToAllUsers(Long userId, String eventName, Object data){
        for(LabeledEmitter labeledEmitter : labeledEmitterList) {
            if (!labeledEmitter.getUserId().equals(userId)) {
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

