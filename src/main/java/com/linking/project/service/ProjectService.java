package com.linking.project.service;

import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.project.persistence.ProjectRepository;
import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.project.persistence.ProjectMapper;
import com.linking.project.domain.Project;

import com.linking.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    private final ParticipantRepository participantRepository;

    // TODO: create logic needs to be optimized
    public Optional<ProjectContainsPartsRes> createProject(ProjectCreateReq projectCreateReq)
            throws DataIntegrityViolationException {
        Project project = projectRepository.save(projectMapper.toEntity(projectCreateReq));
        List<Long> participantIdList = projectCreateReq.getPartList();
        Participant.ParticipantBuilder participantBuilder = Participant.builder();
        for(int i = 0; i < projectCreateReq.getPartList().size(); i++) {
            participantRepository.save(
                    participantBuilder
                            .user(new User(participantIdList.get(i)))
                            .project(project).build());
        }
        return projectRepository.findById(project.getProjectId()).map(projectMapper::toDto);
    }

    public Optional<ProjectContainsPartsRes> getProjectsContainingParts(Long projectId)
            throws NoSuchElementException{
        return Optional.ofNullable(projectRepository.findById(projectId)
                .map(projectMapper::toDto)
                .orElseThrow(NoSuchElementException::new));
    }

    public List<ProjectContainsPartsRes> getProjectsByOwnerId(Long ownerId)
            throws NoSuchElementException{
        List<Project> data = projectRepository.findByOwner(ownerId);
        if(data.isEmpty())
            throw new NoSuchElementException();
        return projectMapper.toDto(data);
    }

    public Optional<ProjectContainsPartsRes> updateProject(ProjectUpdateReq projectUpdateReq)
            throws NoSuchElementException{
        if(!projectRepository.existsById(projectUpdateReq.getProjectId()))
            throw new NoSuchElementException();
        return Optional.of(projectMapper.toDto(
                projectRepository.save(
                        projectMapper.toEntity(projectUpdateReq)))
        );
    }

    public void deleteProject(Long projectId)
            throws EmptyResultDataAccessException, SQLIntegrityConstraintViolationException {
        Optional<Project> possibleProject = projectRepository.findById(projectId);
        if(possibleProject.isPresent() && possibleProject.get().getParticipantList().size() > 1)
            throw new SQLIntegrityConstraintViolationException();
        projectRepository.deleteById(projectId);
    }

}
