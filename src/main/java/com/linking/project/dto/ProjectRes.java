package com.linking.project.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectRes {

    private Long projectId;
    private String projectName;
    private LocalDate beginDate;
    private LocalDate dueDate;
    private Long ownerId;
    private List<Long> partList;

}
