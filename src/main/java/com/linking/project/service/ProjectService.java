package com.linking.project.service;

import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.project.dto.ProjectRes;
import com.linking.project.persistence.ProjectRepository;
import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.project.persistence.ProjectMapper;
import com.linking.project.domain.Project;

import com.linking.user.domain.User;
import com.linking.user.dto.UserDetailedRes;
import com.linking.user.persistence.UserMapper;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    public Optional<ProjectContainsPartsRes> createProject(ProjectCreateReq projectCreateReq)
            throws DataIntegrityViolationException {
        Project project = projectRepository.save(projectMapper.toEntity(projectCreateReq));

        List<User> userList = userRepository.findAllById(projectCreateReq.getPartList());
        for(User user : userList) {
            Participant.ParticipantBuilder participantBuilder = Participant.builder();
            participantRepository.save(
                    participantBuilder
                            .project(project)
                            .user(user)
                            .userName(user.getFullName()).build());
        }

        return Optional.ofNullable(projectMapper.toDto(project, userMapper.toDto(userList)));
    }

    public Optional<ProjectContainsPartsRes> getProjectsContainingParts(Long projectId)
            throws NoSuchElementException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(NoSuchElementException::new);
        List<UserDetailedRes> partList = userMapper.toDto(project.getParticipantList().stream()
                        .map(Participant::getUser).collect(Collectors.toList()));

        return Optional.ofNullable(projectMapper.toDto(project, partList));
    }

    public List<ProjectRes> getProjectsByOwnerId(Long ownerId)
            throws NoSuchElementException{
        List<Project> projectList = projectRepository.findByOwner(ownerId);
        if(projectList.isEmpty())
            throw new NoSuchElementException();
        return projectMapper.toDto(projectList);
    }

    public Optional<ProjectContainsPartsRes> updateProject(
            ProjectUpdateReq projectUpdateReq, List<Long> partIdList)
            throws NoSuchElementException{
        Project res =
                projectRepository.save(
                        projectMapper.toEntity(
                                projectUpdateReq, participantRepository.findAllById(partIdList)));
        List<UserDetailedRes> partList = userMapper.toDto(res.getParticipantList().stream()
                .map(Participant::getUser).collect(Collectors.toList()));
        return Optional.ofNullable(projectMapper.toDto(res, partList));
    }

    public void deleteProject(Long projectId)
            throws DataIntegrityViolationException, EmptyResultDataAccessException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(NoSuchElementException::new);
        if(project.getParticipantList().size() > 1)
            throw new DataIntegrityViolationException("삭제할 수 없는 프로젝트");
        projectRepository.deleteById(projectId);
    }

}
