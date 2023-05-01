package com.linking.todo.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParentTodoRes {

    private Long todoId;
    private Boolean isParent;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String content;
    private Long userId;
    private String userName;
    private String status;
    private List<ChildTodoRes> childTodoList;

}
