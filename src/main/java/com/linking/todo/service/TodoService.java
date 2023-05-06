package com.linking.todo.service;

import com.linking.assign.domain.Assign;
import com.linking.assign.domain.Status;
import com.linking.assign.persistence.AssignMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    private final ParticipantRepository participantRepository;
    private final AssignRepository assignRepository;
    private final AssignMapper assignMapper;

    public TodoSingleRes createTodo(TodoCreateReq todoCreateReq){
        log.info("createTodo - {} ", this.getClass().getSimpleName());

        Todo todo = todoRepository.save(todoMapper.toEntity(todoCreateReq));

        List<Participant> participantList =
                participantRepository.findByProjectAndUser(
                        new Project(todoCreateReq.getProjectId()), todoCreateReq.getAssignList());
        Assign.AssignBuilder assignBuilder = Assign.builder();

        List<Assign> assignList = new ArrayList<>();
        for(Participant participant : participantList)
            assignList.add(assignRepository.save(
                    assignBuilder
                            .todo(todo)
                            .participant(participant)
                            .status(Status.BEFORE_START).build()));

        todo.setAssignList(assignList);
        return todoMapper.toResDto(todo);
    }

    public Optional<TodoSingleRes> getTodo(Long id){
        return todoRepository.findById(id)
                .map(todoMapper::toResDto);
    }

    public List<TodoSimpleRes> getTodayUserUrgentTodos(Long id){
        List<Participant> participantList = participantRepository.findByUser(new User(id));
        List<Assign> assignList = assignRepository.findByParticipantAndDate(participantList, LocalDate.now());
        return todoMapper.toSimpleDto(assignList);
    }

    public List<ParentTodoRes> getTodayProjectUrgentTodos(Long id){
        List<Todo> todoList = todoRepository.findByProjectAndMonth(new Project(id), LocalDate.now());
        return todoMapper.toParentDto(todoList);
    }

    public List<ParentTodoRes> getDailyProjectTodos(Long id, int year, int month, int day){
        List<Todo> todoList = todoRepository.findByProjectAndDateContains(new Project(id), LocalDate.of(year, month, day));
        return todoMapper.toParentDto(todoList);
    }

    public List<ParentTodoRes> getMonthlyProjectTodos(Long id, int year, int month){
        List<Todo> todoList = todoRepository.findByProjectAndMonthContains(new Project(id), LocalDate.of(year, month, 1));
        return todoMapper.toParentDto(todoList);
    }

    // TODO: To be deleted
    public List<ParentTodoRes> getDailyUserTodos(Long id, int year, int month, int day){
        List<Participant> participantList = participantRepository.findByUser(new User(id));
        List<Todo> todoList =
                assignRepository.findByParticipantAndDateContains(participantList, LocalDate.of(year, month, day))
                        .stream().map(Assign::getTodo).collect(Collectors.toList());
        return todoMapper.toParentDto(todoList);
    }

    public Optional<TodoSingleRes> updateTodo(TodoUpdateReq todoUpdateReq){
        List<Assign> assignList = todoRepository.findById(
                todoUpdateReq.getTodoId()).orElseThrow(NoSuchElementException::new).getAssignList();
        Todo todo = todoRepository.save(todoMapper.toEntity(todoUpdateReq, assignList.stream().map(Assign::getAssignId).collect(Collectors.toList())));
        return Optional.ofNullable(todoMapper.toResDto(todo));
    }

    public Optional<TodoSingleRes> updateTodo(TodoUpdateReq todoUpdateReq, List<Long> assignIdList){
        Todo todo = todoRepository.save(todoMapper.toEntity(todoUpdateReq, assignIdList));
        return Optional.ofNullable(todoMapper.toResDto(todo));
    }

    public void deleteTodo(Long id){
        todoRepository.deleteById(id);
    }

}
