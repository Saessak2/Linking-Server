package com.linking.project.persistence;

import com.linking.participant.domain.Participant;
import com.linking.project.domain.Project;
import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.dto.ProjectRes;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
//@Mapper(componentModel = "spring")
@Component
public class ProjectMapper {

    private final UserMapper userMapper;
//    default ProjectContainsPartsRes toDto(Project project) {
//        if(project == null)
//            return null;
//
//        // TODO: Builder pattern with List needs to be refactored
//        ProjectContainsPartsRes.ProjectContainsPartsResBuilder projResBuilder
//                = ProjectContainsPartsRes.builder();
//        projResBuilder
//                .projectId(project.getProjectId())
//                .projectName(project.getProjectName())
//                .beginDate(project.getBeginDate())
//                .dueDate(project.getDueDate());
//
////        if(!project.getParticipantList().isEmpty())
////                projResBuilder.partList(project.getParticipantList()
////                        .stream().map(Participant::getUser)
////                        .collect(Collectors.toList()));
//
//        return projResBuilder.build();
//    }

    public ProjectContainsPartsRes toDto(Project project, List<Participant> participantList){
        if(project == null)
            return null;

        ProjectContainsPartsRes.ProjectContainsPartsResBuilder projResBuilder
                = ProjectContainsPartsRes.builder();
        projResBuilder
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .beginDate(project.getBeginDate())
                .dueDate(project.getDueDate())
                .ownerId(project.getOwner().getUserId());

        if(!participantList.isEmpty())
                projResBuilder.partList(
                        userMapper.toDto(
                        participantList.stream()
                                .map(Participant::getUser).collect(Collectors.toList())));



        return projResBuilder.build();
    }

//    default List<ProjectContainsPartsRes> toDto(List<Project> projectList) {
//        if(projectList.isEmpty())
//            return null;
//        return projectList.stream().map(this::toDto).collect(Collectors.toList());
//    }

    public List<ProjectRes> toResDto(List<Project> list){
        List<ProjectRes> list1 = new ArrayList<>();
        for (Project project : list) {
            list1.add(new ProjectRes(project.getProjectId(), project.getProjectName()));
        }
        return list1;
    }

    public Project toEntity(ProjectCreateReq projectCreateReq) {
        if (projectCreateReq == null)
            return null;

        Project.ProjectBuilder projBuilder = Project.builder();
        projBuilder
                .projectName(projectCreateReq.getProjectName())
                .beginDate(projectCreateReq.getBeginDate())
                .dueDate(projectCreateReq.getDueDate())
                .owner(new User(projectCreateReq.getPartList().get(0)))
                .participantList(new ArrayList<>());

        return projBuilder.build();
    }

    public Project toEntity(ProjectUpdateReq projectUpdateReq) {
        if(projectUpdateReq == null)
            return null;

        Project.ProjectBuilder projBuilder = Project.builder();
        projBuilder
                .projectId(projectUpdateReq.getProjectId())
                .projectName(projectUpdateReq.getProjectName())
                .beginDate(projectUpdateReq.getBeginDate())
                .dueDate(projectUpdateReq.getDueDate())
                .owner(new User(projectUpdateReq.getPartList().get(0)))
                .participantList(new ArrayList<>());

        return projBuilder.build();
    }

}
