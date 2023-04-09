package com.linking.project.dto;

import com.linking.user.domain.User;
import com.linking.user.dto.UserDetailedRes;
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
public class ProjectContainsPartsRes {

    private Long projectId;
    private String projectName;
    private LocalDate beginDate;
    private LocalDate dueDate;
    private List<UserDetailedRes> partList;
    // TODO: List<User> -> List<UserDetailedDto> (capsule)
}
