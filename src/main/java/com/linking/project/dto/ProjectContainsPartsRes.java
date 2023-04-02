package com.linking.project.dto;

import com.linking.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProjectContainsPartsRes {

    private Long projectId;
    private String projectName;
    private LocalDate beginDate;
    private LocalDate dueDate;
    private Long ownerId;
    private List<User> partList;

}
