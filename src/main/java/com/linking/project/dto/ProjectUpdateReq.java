package com.linking.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class ProjectUpdateReq {

    @NotNull
    @NotBlank
    private Long projectId;

    @NotNull
    @NotBlank
    private String projectName;

    @NotNull
    @NotBlank
    private LocalDate beginDate;

    @NotNull
    @NotBlank
    private LocalDate dueDate;

}