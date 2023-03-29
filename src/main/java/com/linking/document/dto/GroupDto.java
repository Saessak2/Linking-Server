package com.linking.document.dto;


import com.linking.document.Group;
import com.linking.project.Project;
import com.linking.project.dto.ProjectDto;
import lombok.*;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@Builder
public class GroupDto {

    private String name;
    private int docDepth;
    private int docIndex;
    private GroupDto parentGroupDto;
    private ProjectDto projectDto;

    public GroupDto() {}

    public GroupDto getParentGroupDto() {
        return parentGroupDto;
    }

    public Group toEntity() {

        Group group = Group.builder()
                .name(name)
                .doc_depth(docDepth)
                .doc_index(docDepth)
                .parent()
                .project(project)
                .childDocList(new ArrayList<>())
                .build();

        return group;
    }
}
