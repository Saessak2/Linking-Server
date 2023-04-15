package com.linking.participant.service;

import com.linking.pageCheck.service.PageCheckService;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.dto.ParticipantIdReq;
import com.linking.participant.dto.ParticipantDeleteReq;
import com.linking.participant.dto.ParticipantRes;
import com.linking.participant.persistence.ParticipantMapper;
import com.linking.project.domain.Project;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.persistence.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final PageCheckService pageCheckService;
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    private final ProjectMapper projectMapper;

    Logger logger = LoggerFactory.getLogger(ParticipantService.class);


    public Optional<ParticipantRes> createParticipant(ParticipantIdReq participantIdReq)
            throws DataIntegrityViolationException {
        List<Participant> partData = participantRepository.findByUserAndProject(
                participantIdReq.getUserId(), participantIdReq.getProjectId());
        if (!partData.isEmpty())
            throw new DuplicateKeyException("Already in project");

        Participant participant = participantRepository.save(participantMapper.toEntity(participantIdReq));
        pageCheckService.createPageCheckForAddParticipant(participant);

        return Optional.of(participantMapper.toDto(participant));
    }

    public Optional<ParticipantRes> getParticipant(Long participantId)
            throws NoSuchElementException {
        return Optional.ofNullable(participantRepository.findById(participantId)
                .map(participantMapper::toDto)
                .orElseThrow(NoSuchElementException::new));
    }

    public List<ParticipantRes> getParticipantsByProjectId(Long projectId)
            throws NoSuchElementException {
        List<Participant> participantList = participantRepository.findByProject(new Project(projectId));
        if (participantList.isEmpty())
            throw new NoSuchElementException();
        return participantMapper.toDto(participantList);
    }

    public List<ProjectContainsPartsRes> getPartsByUserId(Long userId)
            throws NoSuchElementException {
        List<Project> projectList = participantRepository.findProjectsByUser(userId);
        if (projectList.isEmpty())
            throw new NoSuchElementException();
//        List<ProjectRes> projectList = new ArrayList<>();
//        for (Participant participant : participantList) {
//            projectList.add(new ProjectRes(
//                    participant.getParticipantId(),
//                    participant.getProject().getProjectName()));
//        }
        List<ProjectContainsPartsRes> result = new ArrayList<>();
        for (Project project : projectList) {
            result.add(projectMapper.toDto(project, project.getParticipantList()));
        }

        return result;
    }

    public void deleteParticipant(ParticipantDeleteReq participantDeleteReq)
            throws NoSuchElementException, SQLIntegrityConstraintViolationException {
        List<Participant> participantList = setParticipantList(participantDeleteReq.getPartIdList());
        if(participantList.isEmpty())
            throw new NoSuchElementException();
        if(containsOwner(participantList)) {
            logger.info("\ncontains Owner ===================+> cannot delete participants");
            throw new SQLIntegrityConstraintViolationException();
        }
        participantRepository.deleteAll(participantList);
    }

    private List<Participant> setParticipantList(List<ParticipantIdReq> participantIdReqList){
        List<Participant> participantList = new ArrayList<>();
        for(ParticipantIdReq p : participantIdReqList){
            participantList.addAll(
                    participantRepository.findByUserAndProject(
                            p.getUserId(), p.getProjectId()));
        }
        return participantList;
    }

    private boolean containsOwner(List<Participant> participantList){
        for(Participant p: participantList){
            if(p.getProject().getOwner().getUserId().equals(p.getUser().getUserId()))
                return true;
        }
        return false;
    }

    /**
     * 작성자 : 이은빈
     */
    public Optional<Participant> getParticipant(Long userId, Long projectId) {
        return participantRepository.findByUserAndProjectId(userId, projectId);
    }
}
