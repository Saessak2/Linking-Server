package com.linking.project;

import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.project.mapper.ProjectMapper;
import com.linking.project.domain.Project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public Optional<ProjectContainsPartsRes> createProject(ProjectCreateReq projectCreateReq){
        return Optional.of(projectMapper.toDto(
                projectRepository.save(
                        projectMapper.toEntity(projectCreateReq)))
        );
    }

    public Optional<ProjectContainsPartsRes> getProject(Long projectId) throws NoResultException{
        Optional<Project> data = projectRepository.findById(projectId);
        if(data.isEmpty())
            throw new NoResultException();
        return Optional.of(projectMapper.toDto(data.get()));
    }

    public List<ProjectContainsPartsRes> getProjectsByOwnerId(Long ownerId) throws NoResultException{
        List<Project> data = projectRepository.findByOwner(ownerId);
        if(data.isEmpty())
            throw new NoResultException();
        return projectMapper.toDto(data);
    }

    public Optional<ProjectContainsPartsRes> updateProject(ProjectUpdateReq projectUpdateReq){
        return Optional.of(projectMapper.toDto(
                projectRepository.save(
                        projectMapper.toEntity(projectUpdateReq)))
        );
    }

    public Optional<ProjectContainsPartsRes> deleteProject(Long projectId) throws NoResultException{
        Optional<Project> data = projectRepository.findById(projectId);
        if(data.isEmpty())
            throw new NoResultException();
        projectRepository.deleteById(projectId);
        return Optional.of(projectMapper.toDto(data.get()));
    }

}
