package com.linking.todo.dto;

import com.linking.assign.dto.AssignRes;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildTodoRes {

    private Long todoId;
    private Boolean isParent;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String content;
    private List<AssignRes> assignList;

}