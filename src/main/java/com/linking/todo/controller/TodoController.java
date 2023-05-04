package com.linking.todo.controller;

import com.linking.assign.service.AssignService;
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

    private final AssignService assignService;

    @GetMapping("/connect/{clientType}/{userId}")
    public ResponseEntity<SseEmitter> connect(@PathVariable String clientType, @PathVariable Long userId){
        LabeledEmitter labeledEmitter = todoSseHandler.connect(clientType, userId);
        SseEmitter sseEmitter = labeledEmitter.getSseEmitter();
        try{
            sseEmitter
                    .send(SseEmitter.event()
                    .name("CONNECT")
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

    @GetMapping("/single/{id}")
    public ResponseEntity<Object> getTodo(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                todoService.getTodo(id));
    }

    @GetMapping("/list/today/user/{id}")
    public ResponseEntity<Object> getTodayUserTodos(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                todoService.getTodayUserTodos(id));
    }

    @GetMapping("/list/daily/user/{id}/{year}/{month}/{day}")
    public ResponseEntity<Object> getDailyUserTodos(@PathVariable Long id,
            @PathVariable int year, @PathVariable int month, @PathVariable int day){
        return ResponseHandler.generateOkResponse(
                todoService.getDailyUserTodos(id, year, month, day));
    }

    @GetMapping("/list/daily/project/{id}/{year}/{month}/{day}")
    public ResponseEntity<Object> getDailyProjectTodos(@PathVariable Long id,
            @PathVariable int year, @PathVariable int month, @PathVariable int day){
        return ResponseHandler.generateOkResponse(
                todoService.getTodayProjectTodos(id, year, month, day));
    }

    @GetMapping("/list/monthly/project/{id}/{year}/{month}")
    public ResponseEntity<Object> getMonthlyProjectTodos(
            @PathVariable Long id, @PathVariable int year, @PathVariable int month){
        return ResponseHandler.generateOkResponse(
                todoService.getMonthlyProjectTodos(id, year, month));
    }

    @PutMapping
    public ResponseEntity<Object> putTodo(@RequestBody @Valid TodoUpdateReq todoUpdateReq){
        TodoRes todoRes;
        if(!todoUpdateReq.getIsAssignListChanged())
            todoRes = todoService.updateTodo(todoUpdateReq).get();
        else
            todoRes = todoService.updateTodo(
                        todoUpdateReq, assignService.updateAssignList(todoUpdateReq)).get();
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
