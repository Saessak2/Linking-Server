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
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    public Optional<ProjectContainsPartsRes> createProject(ProjectCreateReq projectCreateReq)
            throws DataIntegrityViolationException {
        Project project = projectRepository.save(projectMapper.toEntity(projectCreateReq));
        Participant.ParticipantBuilder participantBuilder = Participant.builder();
        List<Participant> participantList = new ArrayList<>();
        for(Long id : projectCreateReq.getPartList()) {
            participantList.add(
                    participantRepository.save(
                            participantBuilder
                                    .user(userRepository.findById(id).get())
                                    .project(project).build()));
        }
        return Optional.ofNullable(projectMapper.toDto(project, participantList));
    }

    public Optional<ProjectContainsPartsRes> getProjectsContainingParts(Long projectId)
            throws NoSuchElementException {
        Optional<Project> possibleProject = projectRepository.findById(projectId);
        List<Participant> participantList = participantRepository.findByProject(new Project(projectId));
        return Optional.ofNullable(possibleProject
                .map(p -> projectMapper.toDto(p, participantList))
                .orElseThrow(NoSuchElementException::new));
    }

    public List<ProjectContainsPartsRes> getProjectsByOwnerId(Long ownerId)
            throws NoSuchElementException{
        List<Project> projectList = projectRepository.findByOwner(ownerId);
        if(projectList.isEmpty())
            throw new NoSuchElementException();
//        List<Long> projectIdList = projectList.stream().map(Project::getProjectId).collect(Collectors.toList());
        List<ProjectContainsPartsRes> projectResList = new ArrayList<>();
        for(Project p : projectList)
            projectResList.add(projectMapper.toDto(
                    p, participantRepository.findByProject(p)));
        return projectResList;
    }

    public Optional<ProjectContainsPartsRes> updateProject(ProjectUpdateReq projectUpdateReq)
            throws NoSuchElementException{
        if(!projectRepository.existsById(projectUpdateReq.getProjectId()))
            throw new NoSuchElementException();
        Project project = projectRepository.save(projectMapper.toEntity(projectUpdateReq));
        List<Participant> participantList = participantRepository.findByProject(project);

        return Optional.of(projectMapper.toDto(project, participantList));
    }

    public void deleteProject(Long projectId)
            throws EmptyResultDataAccessException, SQLIntegrityConstraintViolationException {
        Optional<Project> possibleProject = projectRepository.findById(projectId);
        List<Participant> participantList = participantRepository.findByProject(new Project(projectId));
        if(possibleProject.isPresent() && participantList.size() > 1)
            throw new SQLIntegrityConstraintViolationException();
        projectRepository.deleteById(projectId);
    }

}
