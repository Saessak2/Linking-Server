package com.linking.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class ProjectRes {

    private Long projectId;
    private String projectName;
    private LocalDate beginDate;
    private LocalDate dueDate;
    private Long ownerId;

}
