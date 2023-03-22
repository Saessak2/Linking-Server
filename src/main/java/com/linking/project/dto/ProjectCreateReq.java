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
public class ProjectCreateReq {

    @NotBlank
    private String projectName;

    @NotNull
    @NotBlank
    private LocalDate beginDate;

    @NotNull
    @NotBlank
    private LocalDate dueDate;

}
