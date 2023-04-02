package com.linking.project.mapper;

import com.linking.participant.domain.Participant;
import com.linking.project.domain.Project;
import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.user.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    default ProjectContainsPartsRes toDto(Project project) {
        if(project == null)
            return null;

        ProjectContainsPartsRes.ProjectContainsPartsResBuilder projResBuilder
                = ProjectContainsPartsRes.builder();
        projResBuilder
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .beginDate(project.getBeginDate())
                .dueDate(project.getDueDate())
                .ownerId(project.getOwner().getUserId())
                .partList(project.getParticipant()
                        .stream().map(Participant::getUser)
                        .collect(Collectors.toList()));

        return projResBuilder.build();
    }

    default List<ProjectContainsPartsRes> toDto(List<Project> projectList) {
        if(projectList.isEmpty())
            return null;
        return projectList.stream().map(this::toDto).collect(Collectors.toList());
    }

    default Project toEntity(ProjectCreateReq projectCreateReq) {
        if (projectCreateReq == null)
            return null;

        Project.ProjectBuilder projBuilder = Project.builder();
        projBuilder
                .projectName(projectCreateReq.getProjectName())
                .beginDate(projectCreateReq.getBeginDate())
                .dueDate(projectCreateReq.getDueDate())
                .owner(new User(projectCreateReq.getOwnerId()));

        return projBuilder.build();
    }

    default Project toEntity(ProjectUpdateReq projectUpdateReq) {
        if(projectUpdateReq == null)
            return null;

        Project.ProjectBuilder projBuilder = Project.builder();
        projBuilder
                .projectId(projectUpdateReq.getOwnerId())
                .projectName(projectUpdateReq.getProjectName())
                .beginDate(projectUpdateReq.getBeginDate())
                .dueDate(projectUpdateReq.getDueDate())
                .owner(new User(projectUpdateReq.getOwnerId()));

        return projBuilder.build();
    }

}
