package com.linking.todo.controller;

import com.linking.global.common.LabeledEmitter;
import com.linking.global.common.ResponseHandler;
import com.linking.todo.dto.TodoCreateReq;
import com.linking.todo.dto.TodoDeleteReq;
import com.linking.todo.dto.TodoRes;
import com.linking.todo.dto.TodoUpdateReq;
import com.linking.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoSseHandler todoSseHandler;
    private final TodoService todoService;

    @GetMapping("/connect/{userId}")
    public ResponseEntity<SseEmitter> connect(@PathVariable Long userId){
        LabeledEmitter labeledEmitter = todoSseHandler.connect(userId);
        SseEmitter sseEmitter = labeledEmitter.getSseEmitter();
        try{
            sseEmitter
                    .send(SseEmitter.event()
                    .name("connect")
                    .data(labeledEmitter.getEmitterId()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(sseEmitter);
    }

    @GetMapping("/disconnect/{emitterId}")
    public void disconnect(@PathVariable int emitterId){
        todoSseHandler.disconnect(emitterId);
    }


    @PostMapping("/new")
    public ResponseEntity<Object> postTodo(@RequestBody @Valid TodoCreateReq todoCreateReq){
        TodoRes todoRes = todoService.createTodo(todoCreateReq);
        todoSseHandler.send(todoCreateReq.getEmitterId(), "TODO-POSTED", todoRes);
        return ResponseHandler.generateCreatedResponse(todoRes.getTodoId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTodo(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                todoService.getTodo(id));
    }

    @GetMapping("/list/today/user/{id}")
    public ResponseEntity<Object> getTodayMyTodos(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                todoService.getTodayMyTodos(id));
    }

    @GetMapping("/list/today/project/{id}")
    public ResponseEntity<Object> getTodayProjectTodos(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                todoService.getTodayProjectTodos(id));
    }

    @GetMapping("/list/monthly/project/{id}")
    public ResponseEntity<Object> getMonthlyProjectTodos(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                todoService.getMonthlyProjectTodos(id));
    }

    @PutMapping
    public ResponseEntity<Object> putTodo(@RequestBody @Valid TodoUpdateReq todoUpdateReq){
        TodoRes todoRes = todoService.updateTodo(todoUpdateReq).get();
        todoSseHandler.send(todoUpdateReq.getEmitterId(), "TODO UPDATED", todoRes);
        return ResponseHandler.generateOkResponse(todoRes.getTodoId());
    }

    @PostMapping
    public ResponseEntity<Object> deleteTodo(@RequestBody TodoDeleteReq todoDeleteReq){
        todoSseHandler.send(todoDeleteReq.getEmitterId(), "TODO DELETED", todoDeleteReq.getTodoId());
        todoService.deleteTodo(todoDeleteReq.getTodoId());
        return ResponseHandler.generateNoContentResponse();
    }

}
