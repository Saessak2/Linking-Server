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

    public LabeledEmitter connect(String clientType, Long projectId, Long userId) throws IOException {
        LabeledEmitter labeledEmitter = new LabeledEmitter(
                ++createdTodoEmitter, userId, projectId, clientType, new SseEmitter(TIMEOUT));
        SseEmitter sseEmitter = addEmitter(labeledEmitter);
        log.info("[TODO][CONNECT] emitterId = {}, clientType = {}, projectId = {}, userId = {}",
                labeledEmitter.getEmitterId(), labeledEmitter.getUserId(), labeledEmitter.getClientType(), sseEmitter);
        sseEmitter.send(SseEmitter.event().name("connect").data(
                new TodoSseConnectData(labeledEmitter.getEmitterId())));

        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onCompletion(() -> {
            log.info("[TODO][REMOVE] emitterId = {}, userId = {}",
                    labeledEmitter.getEmitterId(), labeledEmitter.getUserId());
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

    public void send(int emitterId, Long projectId, String eventName, Object data) {
        if(labeledEmitterList.isEmpty())
            return;
        if(emitterId == -1)
            sendToAllEmittersFromAnonymous(eventName, data);
        else {
            LabeledEmitter labeledEmitter =
                    labeledEmitterList.stream().filter(e -> e.getEmitterId() == emitterId)
                            .findAny().orElseThrow(NoSuchElementException::new);
            if (labeledEmitter.getClientType().equals("web")) {
                sendToAllEmittersFromWeb(emitterId, projectId, eventName, data);
            } else if (labeledEmitter.getClientType().equals("mac")) {
                sendToAllUsersFromMac(labeledEmitter.getUserId(), projectId, eventName, data);
            }
        }
    }

    private void sendToAllEmittersFromAnonymous(String eventName, Object data){
        for(LabeledEmitter labeledEmitter : labeledEmitterList) {
            try {
                labeledEmitter.getSseEmitter()
                        .send(SseEmitter.event().name(eventName).data(data));
                log.info("[TODO][SEND] EVENT {} FROM anonymous", eventName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendToAllEmittersFromWeb(int emitterId, Long projectId, String eventName, Object data){
        for(LabeledEmitter labeledEmitter : labeledEmitterList) {
            if (labeledEmitter.getEmitterId() != emitterId && labeledEmitter.getProjectId().equals(projectId)) {
                try {
                    labeledEmitter.getSseEmitter()
                            .send(SseEmitter.event().name(eventName).data(data));
                    log.info("[TODO][SEND] EVENT {} FROM web", eventName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void sendToAllUsersFromMac(Long userId, Long projectId, String eventName, Object data){
        for(LabeledEmitter labeledEmitter : labeledEmitterList) {
            if (!labeledEmitter.getUserId().equals(userId) && labeledEmitter.getProjectId().equals(projectId)) {
                try {
                    labeledEmitter.getSseEmitter()
                            .send(SseEmitter.event().name(eventName).data(data));
                    log.info("SEND {} EVENT FROM mac", eventName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}

