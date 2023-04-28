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


    /**
     * 이은빈 수정
     */
    public Optional<ProjectContainsPartsRes> createProject(ProjectCreateReq projectCreateReq)
            throws DataIntegrityViolationException {
        Project project = projectRepository.save(projectMapper.toEntity(projectCreateReq));

        for(Long id : projectCreateReq.getPartList()) {
            Participant participant = new Participant();
            participant.setUser(userRepository.findById(id).get());
            participant.setProject(project);
            participantRepository.save(participant);
        }
        System.out.println("project = " + project);
        return Optional.ofNullable(projectMapper.toDto(project, project.getParticipantList()));
    }

    /**
     * 이은빈 수정
     */
    public Optional<ProjectContainsPartsRes> getProjectsContainingParts(Long projectId)
            throws NoSuchElementException {
        Optional<Project> possibleProject = projectRepository.findById(projectId);
        List<Participant> participantList = possibleProject.get().getParticipantList();

//        List<Participant> participantList = participantRepository.findByProject(new Project(projectId));
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

    /**
     * 이은빈 수정
     */
    public Optional<ProjectContainsPartsRes> updateProject(ProjectUpdateReq projectUpdateReq)
            throws NoSuchElementException{

        Project project = projectRepository.save(projectMapper.toEntity(projectUpdateReq));
        List<Participant> participantList = project.getParticipantList();

        return Optional.of(projectMapper.toDto(project, participantList));
    }

    public void deleteProject(Long projectId)
            throws EmptyResultDataAccessException, SQLIntegrityConstraintViolationException {
        Project possibleProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException());
        List<Participant> participantList = possibleProject.getParticipantList();

//        List<Participant> participantList = participantRepository.findByProject(new Project(projectId));
        // TODO 이건 팀원이 있을 경우에 프로젝트를 함부로 못지우게 하기 위함인가?
        if(participantList.size() > 1)
            throw new SQLIntegrityConstraintViolationException();
        projectRepository.deleteById(projectId);
    }
}
