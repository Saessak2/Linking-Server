package com.linking.todo.controller;

import com.linking.assign.dto.AssignRes;
import com.linking.assign.service.AssignService;
import com.linking.global.common.LabeledEmitter;
import com.linking.global.common.ResponseHandler;
import com.linking.todo.dto.*;
import com.linking.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
@Slf4j
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
                    .name("connect")
                    .data(new TodoSseConnectRes(labeledEmitter.getEmitterId())));
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
        TodoSingleRes todoSingleRes = todoService.createTodo(todoCreateReq);
        List<Long> idList = new ArrayList<>();
        idList.add(todoSingleRes.getTodoId());
        idList.addAll(todoSingleRes.getAssignList().stream().map(AssignRes::getAssignId).collect(Collectors.toList()));
        return ResponseHandler.generateCreatedResponse(idList);
    }

    @GetMapping("/single/{id}")
    public ResponseEntity<Object> getTodo(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                todoService.getTodo(id));
    }

    @GetMapping("/list/today/user/{id}/urgent")
    public ResponseEntity<Object> getTodayUserUrgentTodos(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                todoService.getTodayUserUrgentTodos(id));
    }

    @GetMapping("/list/today/project/{id}/urgent")
    public ResponseEntity<Object> getTodayProjectUrgentTodos(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                todoService.getTodayProjectUrgentTodos(id));
    }

    @GetMapping("/list/daily/project/{id}/{year}/{month}/{day}")
    public ResponseEntity<Object> getDailyProjectTodos(@PathVariable Long id,
            @PathVariable int year, @PathVariable int month, @PathVariable int day){
        return ResponseHandler.generateOkResponse(
                todoService.getDailyProjectTodos(id, year, month, day));
    }

    @GetMapping("/list/monthly/project/{id}/{year}/{month}")
    public ResponseEntity<Object> getMonthlyProjectTodos(
            @PathVariable Long id, @PathVariable int year, @PathVariable int month){
        return ResponseHandler.generateOkResponse(
                todoService.getMonthlyProjectTodos(id, year, month));
    }

    // TODO: To be deleted
    @GetMapping("/list/daily/user/{id}/{year}/{month}/{day}")
    public ResponseEntity<Object> getDailyUserTodos(@PathVariable Long id,
            @PathVariable int year, @PathVariable int month, @PathVariable int day){
        return ResponseHandler.generateOkResponse(
                todoService.getDailyUserTodos(id, year, month, day));
    }

    @PutMapping
    public ResponseEntity<Object> putTodo(@RequestBody @Valid TodoUpdateReq todoUpdateReq){
        TodoSingleRes todoSingleRes;
        if(!todoUpdateReq.getIsAssignListChanged())
            todoSingleRes = todoService.updateTodo(todoUpdateReq).get();
        else
            todoSingleRes = todoService.updateTodo(
                        todoUpdateReq, assignService.updateAssignList(todoUpdateReq)).get();
        return ResponseHandler.generateOkResponse(todoSingleRes.getTodoId());
    }

    @PostMapping
    public ResponseEntity<Object> deleteTodo(@RequestBody TodoDeleteReq todoDeleteReq){
        todoService.deleteTodo(todoDeleteReq.getTodoId());
        return ResponseHandler.generateNoContentResponse();
    }

}
