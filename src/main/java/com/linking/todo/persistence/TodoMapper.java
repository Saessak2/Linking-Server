package com.linking.todo.persistence;

import com.linking.assign.domain.Assign;
import com.linking.assign.dto.AssignRes;
import com.linking.assign.persistence.AssignMapper;
import com.linking.assign.persistence.AssignRepository;
import com.linking.project.domain.Project;
import com.linking.todo.domain.Todo;
import com.linking.todo.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TodoMapper {

    private final AssignRepository assignRepository;
    private final AssignMapper assignMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a").withLocale(Locale.ENGLISH);

    public Todo toEntity(TodoCreateReq todoCreateReq){
        if(todoCreateReq == null)
            return null;

        Todo.TodoBuilder todoBuilder = Todo.builder();
        return todoBuilder
                .project(new Project(todoCreateReq.getProjectId()))
                .isParent(todoCreateReq.getIsParent())
                .startDate(LocalDateTime.parse(todoCreateReq.getStartDate(), formatter))
                .dueDate(LocalDateTime.parse(todoCreateReq.getDueDate(), formatter))
                .content(todoCreateReq.getContent()).build();
    }

    public Todo toEntity(TodoUpdateReq todoUpdateReq, List<Long> assignIdList){
        if(todoUpdateReq == null)
            return null;

        List<Assign> assignList = assignRepository.findAllById(assignIdList);
        Todo.TodoBuilder todoBuilder = Todo.builder();
        return todoBuilder
                .todoId(todoUpdateReq.getTodoId())
                .project(new Project(todoUpdateReq.getProjectId()))
                .parentTodo(new Todo(todoUpdateReq.getTodoId()))
                .isParent(todoUpdateReq.getIsParent())
                .startDate(LocalDateTime.parse(todoUpdateReq.getStartDate(), formatter))
                .dueDate(LocalDateTime.parse(todoUpdateReq.getDueDate(), formatter))
                .content(todoUpdateReq.getContent())
                .assignList(assignList).build();
    }

    public TodoRes toResDto(Todo todo){
        if(todo == null)
            return null;

        TodoRes.TodoResBuilder todoResBuilder = TodoRes.builder();
        return todoResBuilder
                .todoId(todo.getTodoId())
                .isParent(todo.isParent())
                .parentId(todo.getTodoId() == null ? todo.getParentTodo().getTodoId(): -1L)
                .startDate(todo.getStartDate().format(formatter))
                .dueDate(todo.getDueDate().format(formatter))
                .content(todo.getContent())
                .assignList(assignMapper.toResDto(todo.getAssignList())).build();
    }

    public List<TodoRes> toResDto(List<Todo> todoList){
        if(todoList == null)
            return null;
        return todoList.stream().map(this::toResDto).collect(Collectors.toList());
    }

    public ParentTodoRes toParentDto(Todo todo){
        if(todo == null)
            return null;

        ParentTodoRes.ParentTodoResBuilder parentTodoResBuilder = ParentTodoRes.builder();
        return parentTodoResBuilder
                .todoId(todo.getTodoId())
                .isParent(todo.isParent())
                .startDate(todo.getStartDate().format(formatter))
                .dueDate(todo.getDueDate().format(formatter))
                .content(todo.getContent())
                .childTodoList(toResDto(todo.getChildTodoList()))
                .assignList(assignMapper.toResDto(todo.getAssignList())).build();
    }

    public List<ParentTodoRes> toParentDto(List<Todo> todoList){
        if(todoList == null)
            return null;
        return todoList.stream().map(this::toParentDto).collect(Collectors.toList());
    }

}
