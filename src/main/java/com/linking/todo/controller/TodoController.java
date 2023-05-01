package com.linking.todo.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.todo.dto.TodoCreateReq;
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

    @GetMapping("/connect")
    public ResponseEntity<SseEmitter> connect(){
        SseEmitter emitter = new SseEmitter(60 * 1000L);
        todoSseHandler.add(emitter);
        try{
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }

    @GetMapping("/disconnect")
    public void disconnect(){

    }

    /* ----- */

    @PostMapping
    public ResponseEntity<Object> postTodo(@RequestBody @Valid TodoCreateReq todoCreateReq){
        return ResponseHandler.generateCreatedResponse(
                todoService.createTodo(todoCreateReq));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTodo(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                todoService.getTodo(id));
    }

    @GetMapping("/list/today/user/{id}")
    public ResponseEntity<Object> getTodayMyTodos(@PathVariable Long id){
        todoService.getTodayMyTodos(id);
        return (ResponseEntity<Object>) ResponseEntity.ok();
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

    // 할 일 수정
    @PutMapping
    public ResponseEntity<Object> putTodo(@RequestBody @Valid TodoUpdateReq todoUpdateReq){
        return ResponseHandler.generateOkResponse(
                todoService.updateTodo(todoUpdateReq));
    }

    // 할 일 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTodo(@PathVariable Long id){
        todoService.deleteTodo(id);
        return ResponseHandler.generateNoContentResponse();
    }

}
