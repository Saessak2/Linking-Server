package com.linking.project.mapper;

import com.linking.project.domain.Project;
import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectRes;
import com.linking.project.dto.ProjectUpdateReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    public ProjectRes toRes(Project project);
    public Project toProject(ProjectCreateReq projectCreateReq);
    public Project toProject(ProjectUpdateReq projectUpdateReq);

}
