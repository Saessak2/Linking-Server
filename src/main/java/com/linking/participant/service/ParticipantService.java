package com.linking.participant.service;

import com.linking.participant.persistence.ParticipantRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.dto.ParticipantIdReq;
import com.linking.participant.dto.ParticipantDeleteReq;
import com.linking.participant.dto.ParticipantRes;
import com.linking.participant.persistence.ParticipantMapper;
import com.linking.project.domain.Project;
import com.linking.project.dto.ProjectRes;
import com.linking.project.persistence.ProjectMapper;
import com.linking.user.domain.User;
import lombok.RequiredArgsConstructor;
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

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    private final ProjectMapper projectMapper;

    public Optional<ParticipantRes> createParticipant(ParticipantIdReq participantIdReq)
            throws DataIntegrityViolationException {
        List<Participant> partData = participantRepository.findByUserAndProject(
                participantIdReq.getUserId(), participantIdReq.getProjectId());
        if (!partData.isEmpty())
            throw new DuplicateKeyException("Already in project");

        return Optional.of(participantMapper.toDto(
                participantRepository.save(
                        participantMapper.toEntity(participantIdReq))));
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

    public List<ProjectRes> getPartsByUserId(Long userId)
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
        return projectMapper.toResDto(projectList);
//        return projectList;
    }

    public void deleteParticipant(ParticipantDeleteReq participantDeleteReq)
            throws NoSuchElementException, SQLIntegrityConstraintViolationException {
        List<Participant> participantList = setParticipantList(participantDeleteReq.getPartIdList());
        if(participantList.isEmpty())
            throw new NoSuchElementException();
        if(containsOwner(participantList))
            throw new SQLIntegrityConstraintViolationException();
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

}
