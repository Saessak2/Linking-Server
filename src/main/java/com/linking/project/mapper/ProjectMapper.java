package com.linking.project.mapper;

import com.linking.project.domain.Project;
import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectRes;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.user.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    default ProjectRes toRes(Project project) {
        if(project == null)
            return null;

        ProjectRes.ProjectResBuilder projectRes = ProjectRes.builder();

        projectRes.projectId(project.getProjectId());
        projectRes.projectName(project.getProjectName());
        projectRes.beginDate(project.getBeginDate());
        projectRes.dueDate(project.getDueDate());
        projectRes.ownerId(project.getOwner().getUserId());

        return projectRes.build();
    }

    default Project toProject(ProjectCreateReq projectCreateReq) {
        if (projectCreateReq == null)
            return null;

        Project project = new Project();
        project.setProjectName(projectCreateReq.getProjectName());
        project.setBeginDate(projectCreateReq.getBeginDate());
        project.setDueDate(projectCreateReq.getDueDate());
        project.setOwner(new User(projectCreateReq.getOwnerId()));

        return project;
    }

    default Project toProject(ProjectUpdateReq projectUpdateReq) {
        if(projectUpdateReq == null)
            return null;
        return new Project(projectUpdateReq.getProjectId(), projectUpdateReq.getProjectName(),
                projectUpdateReq.getBeginDate(), projectUpdateReq.getDueDate(),
                new User(projectUpdateReq.getOwnerId()));
    }

    default List<ProjectRes> toRes(List<Project> projectList) {
        if(projectList.isEmpty())
            return null;
//        List<ProjectRes> resList = new ArrayList<>();
//        for (Project project : projectList) {
//            resList.add(
//                    new ProjectRes(project.getProjectId(), project.getProjectName(),
//                            project.getBeginDate(), project.getDueDate(), project.getOwner().getUserId()));
//        }
        return projectList.stream().map(this::toRes).collect(Collectors.toList());
//        return resList;
    }

}
