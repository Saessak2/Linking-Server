package com.linking.todo.service;

import com.linking.assign.dto.AssignSseUpdateData;
import com.linking.todo.controller.TodoSseHandler;
import com.linking.todo.dto.TodoSseConnectData;
import com.linking.todo.dto.TodoSseDeleteData;
import com.linking.todo.dto.TodoSsePostData;
import com.linking.todo.dto.TodoSseUpdateData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TodoSseEventHandler {

    private final TodoSseHandler todoSseHandler;

    @Async("eventCallExecutor")
    public void connect(int emitterId){
        todoSseHandler.send(emitterId, "connect", new TodoSseConnectData(emitterId));
    }

    @Async("eventCallExecutor")
    public void postParent(int emitterId, TodoSsePostData todoSsePostData){
        todoSseHandler.send(emitterId, "postParent", todoSsePostData);
    }

    @Async("eventCallExecutor")
    public void postChild(int emitterId, TodoSsePostData todoSsePostData){
        todoSseHandler.send(emitterId, "postChild", todoSsePostData);
    }

    @Async("eventCallExecutor")
    public void updateParent(int emitterId, TodoSseUpdateData todoSseUpdateData){
        todoSseHandler.send(emitterId, "updateParent", todoSseUpdateData);
    }

    @Async("eventCallExecutor")
    public void updateChild(int emitterId, TodoSseUpdateData todoSseUpdateData){
        todoSseHandler.send(emitterId, "updateChild", todoSseUpdateData);
    }

    @Async("eventCallExecutor")
    public void updateParentStatus(int emitterId, AssignSseUpdateData assignSseUpdateData){
        todoSseHandler.send(emitterId, "updateParentStatus", assignSseUpdateData);
    }

    @Async("eventCallExecutor")
    public void updateChildStatus(int emitterId, AssignSseUpdateData assignSseUpdateData){
        todoSseHandler.send(emitterId, "updateChildStatus", assignSseUpdateData);
    }

    @Async("eventCallExecutor")
    public void deleteParent(int emitterId, TodoSseDeleteData todoSseDeleteData){
        todoSseHandler.send(emitterId, "deleteParent", todoSseDeleteData);
    }

    @Async("eventCallExecutor")
    public void deleteChild(int emitterId, TodoSseDeleteData todoSseDeleteData){
        todoSseHandler.send(emitterId, "deleteChild", todoSseDeleteData);
    }

}
