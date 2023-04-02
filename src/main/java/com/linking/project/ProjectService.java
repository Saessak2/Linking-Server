package com.linking.project;

import com.linking.participant.ParticipantRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.dto.ParticipantCreateReq;
import com.linking.participant.mapper.ParticipantMapper;
import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.project.mapper.ProjectMapper;
import com.linking.project.domain.Project;

import com.linking.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
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
    private final ParticipantMapper participantMapper;

    // TODO: create logic needs to be optimized
    public Optional<ProjectContainsPartsRes> createProject(ProjectCreateReq projectCreateReq)
            throws DataIntegrityViolationException {
        Project project = projectRepository.save(projectMapper.toEntity(projectCreateReq));

        for(int i = 0; i < projectCreateReq.getPartList().size(); i++) {
            participantRepository.save(
                    participantMapper.toEntity(
                            new ParticipantCreateReq(
                                    new User(projectCreateReq.getPartList().get(i)), project)));
        }
        return Optional.ofNullable(projectMapper.toDto(projectRepository.findById(project.getProjectId()).get()));
    }

    public Optional<ProjectContainsPartsRes> getProject(Long projectId) throws NoSuchElementException{
        return Optional.ofNullable(projectRepository.findById(projectId)
                .map(projectMapper::toDto)
                .orElseThrow(NoSuchElementException::new));
    }

    public List<ProjectContainsPartsRes> getProjectsByOwnerId(Long ownerId) throws NoSuchElementException{
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

    public Optional<ProjectContainsPartsRes> deleteProject(Long projectId)
            throws NoSuchElementException, SQLIntegrityConstraintViolationException {
        Optional<Project> data = projectRepository.findById(projectId);

        if(data.isEmpty())
            throw new NoSuchElementException();
        if(data.get().getParticipantList().size() > 1)
            throw new SQLIntegrityConstraintViolationException();

        projectRepository.deleteById(projectId);
        return Optional.of(projectMapper.toDto(data.get()));
    }

}
