package com.linking.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linking.assign.dto.AssignRes;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoRes {

    private Long todoId;
    private Boolean isParent;
    private Long parentId;
    private String startDate;
    private String dueDate;
    private String content;
    private List<AssignRes> assignList;

}
