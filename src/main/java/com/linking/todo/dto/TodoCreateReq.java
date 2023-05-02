package com.linking.todo.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoCreateReq {

    @NotNull
    private Long projectId;

    private Long parentId;

    @NotNull
    private Boolean isParent;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull
    private String content;

    @NotNull
    private List<Long> assignList;

}
