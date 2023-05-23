package com.linking.domain.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.linking.domain.assign.dto.AssignRes;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoSseUpdateData {

    private Long todoId;
    private Boolean isParent;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long parentId;

    private String startDate;
    private String dueDate;
    private String content;
    private List<AssignRes> assignList;

}
