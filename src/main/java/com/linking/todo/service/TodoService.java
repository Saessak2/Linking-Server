package com.linking.todo.service;

import com.linking.assign.domain.Assign;
import com.linking.assign.domain.Status;
import com.linking.assign.persistence.AssignRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.project.domain.Project;
import com.linking.todo.domain.Todo;
import com.linking.todo.dto.*;
import com.linking.todo.persistence.TodoMapper;
import com.linking.todo.persistence.TodoRepository;
import com.linking.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    private final ParticipantRepository participantRepository;
    private final AssignRepository assignRepository;

    public Long createTodo(TodoCreateReq todoCreateReq){
        Todo todo = todoRepository.save(todoMapper.toEntity(todoCreateReq));

        List<Participant> participantList =
                participantRepository.findByProjectAndUser(
                        new Project(todoCreateReq.getProjectId()), todoCreateReq.getAssignList());
        Assign.AssignBuilder assignBuilder = Assign.builder();
        for(Participant participant : participantList)
            assignRepository.save(
                    assignBuilder
                            .todo(todo)
                            .participant(participant)
                            .status(Status.BEFORE_START).build());

        return todo.getTodoId();
    }

    public Optional<TodoRes> getTodo(Long id){
        return todoRepository.findById(id)
                .map(todoMapper::toDto);
    }

    public List<TodoSimplifiedRes> getTodayMyTodos(Long id){
        List<Participant> participantList = participantRepository.findByUser(new User(id));
        List<Assign> assignList = assignRepository.findByParticipantAndDate(participantList, LocalDate.now());
        return todoMapper.toDto(assignList);
    }

    public List<ParentTodoRes> getTodayProjectTodos(Long id){
        List<Todo> todoList = todoRepository.findByProjectAndDate(new Project(id), LocalDate.now());
        return todoMapper.toParentDto(todoList);
    }

    public List<ParentTodoRes> getMonthlyProjectTodos(Long id){
        List<Todo> todoList = todoRepository.findByProjectAndMonth(new Project(id), LocalDate.now());
        return todoMapper.toParentDto(todoList);
    }

    public Optional<Long> updateTodo(TodoUpdateReq todoUpdateReq){
        Optional<Todo> possibleTodo = todoRepository.findById(todoUpdateReq.getTodoId());
        if(possibleTodo.isPresent())
            return Optional.ofNullable(todoRepository.save(todoMapper.toEntity(todoUpdateReq)).getTodoId());
        return Optional.empty();
    }

    public void deleteTodo(Long id){
        todoRepository.deleteById(id);
    }

}
