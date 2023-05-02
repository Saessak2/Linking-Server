package com.linking.todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linking.assign.dto.AssignRes;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoRes {

    private Long todoId;
    private Long projectId;
    private Boolean isParent;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String content;
    private List<AssignRes> assignList;

    @JsonIgnore
    public void setAssignList(List<AssignRes> assignList){
        this.assignList = assignList;
    }

}
