package com.linking.todo.persistence;

import com.linking.assign.domain.Assign;
import com.linking.assign.persistence.AssignMapper;
import com.linking.project.domain.Project;
import com.linking.todo.domain.Todo;
import com.linking.todo.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TodoMapper {

    private final AssignMapper assignMapper;

    public TodoRes toDto(Todo todo){
        if(todo == null)
            return null;
        TodoRes.TodoResBuilder todoResBuilder = TodoRes.builder();
        return todoResBuilder
                .todoId(todo.getTodoId())
                .projectId(todo.getProject().getProjectId())
                .isParent(todo.isParent())
                .startDate(todo.getStartDate())
                .dueDate(todo.getDueDate())
                .content(todo.getContent())
                .assignList(assignMapper.toDto(todo.getAssignList())).build();
    }

    public TodoSimplifiedRes toDto(Assign assign) {
        if (assign == null)
            return null;

        Todo todo = assign.getTodo();
        TodoSimplifiedRes.TodoSimplifiedResBuilder todoSimplifiedResBuilder = TodoSimplifiedRes.builder();
        return todoSimplifiedResBuilder
                .todoId(todo.getTodoId())
                .projectName(todo.getProject().getProjectName())
                .dueDate(todo.getDueDate())
                .content(todo.getContent())
                .status(String.valueOf(assign.getStatus())).build();
    }

    public List<TodoSimplifiedRes> toDto(List<Assign> assignList){
        if(assignList == null)
            return null;
        return assignList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ParentTodoRes toParentDto(Todo todo){
        if(todo == null)
            return null;

        Assign assign = todo.getAssignList().get(0);
        ParentTodoRes.ParentTodoResBuilder parentTodoResBuilder = ParentTodoRes.builder();
        return parentTodoResBuilder
                .todoId(todo.getTodoId())
                .isParent(todo.isParent())
                .startDate(todo.getStartDate())
                .dueDate(todo.getDueDate())
                .content(todo.getContent())
                .userId(assign.getParticipant().getUser().getUserId())
                .userName(assign.getParticipant().getUserName())
                .childTodoList(toChildDto(todo.getChildTodoList())).build();
    }

    public List<ParentTodoRes> toParentDto(List<Todo> todoList){
        if(todoList == null)
            return null;
        return todoList.stream().map(this::toParentDto).collect(Collectors.toList());
    }

    public ChildTodoRes toChildDto(Todo todo){
        if(todo == null)
            return null;
        ChildTodoRes.ChildTodoResBuilder childTodoResBuilder = ChildTodoRes.builder();
        return childTodoResBuilder
                .todoId(todo.getTodoId())
                .isParent(todo.isParent())
                .startDate(todo.getStartDate())
                .dueDate(todo.getDueDate())
                .content(todo.getContent())
                .assignList(assignMapper.toDto(todo.getAssignList())).build();
    }

    public List<ChildTodoRes> toChildDto(List<Todo> todoList){
        if(todoList == null)
            return null;
        return todoList.stream().map(this::toChildDto).collect(Collectors.toList());
    }

    public Todo toEntity(TodoCreateReq todoCreateReq){
        if(todoCreateReq == null)
            return null;
        Todo.TodoBuilder todoBuilder = Todo.builder();
        return todoBuilder
                .project(new Project(todoCreateReq.getProjectId()))
                .isParent(todoCreateReq.getIsParent())
                .startDate(todoCreateReq.getStartDate())
                .dueDate(todoCreateReq.getDueDate())
                .content(todoCreateReq.getContent()).build();
    }

    public Todo toEntity(TodoUpdateReq todoUpdateReq){
        if(todoUpdateReq == null)
            return null;
        Todo.TodoBuilder todoBuilder = Todo.builder();
        return todoBuilder
                .todoId(todoUpdateReq.getTodoId())
                .project(new Project(todoUpdateReq.getProjectId()))
                .parentTodo(new Todo(todoUpdateReq.getTodoId()))
                .isParent(todoUpdateReq.getIsParent())
                .startDate(todoUpdateReq.getStartDate())
                .dueDate(todoUpdateReq.getDueDate())
                .content(todoUpdateReq.getContent()).build();
    }

}
