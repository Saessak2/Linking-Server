package com.linking.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRes {

    private Long projectId;
    private String projectName;
    private LocalDate beginDate;
    private LocalDate dueDate;
    private List<Long> partList;

}
