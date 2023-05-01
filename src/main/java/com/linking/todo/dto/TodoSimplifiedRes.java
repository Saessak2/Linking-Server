package com.linking.todo.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoSimplifiedRes {

    private Long todoId;
    private String projectName;
    private LocalDate dueDate;
    private String content;
    private String status;

}
