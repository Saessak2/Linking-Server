package com.linking.project;

import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectRes;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.project.mapper.ProjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {

    private ProjectRepository projectRepository;
    private ProjectMapper projectMapper;

    public Optional<ProjectRes> addProject(ProjectCreateReq projectCreateReq){
        return Optional.of(projectMapper.toRes(
                projectRepository.save(
                        projectMapper.toProject(projectCreateReq)))
        );
    }

    // TODO: findProject

//    public Optional<ProjectRes> updateProject(ProjectUpdateReq projectUpdateReq){
//        return Optional.of(projectMapper.toRes())
//    }

}
