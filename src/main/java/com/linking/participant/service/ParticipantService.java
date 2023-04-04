package com.linking.participant.service;

import com.linking.participant.persistence.ParticipantRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.dto.ParticipantIdReq;
import com.linking.participant.dto.ParticipantEntityReq;
import com.linking.participant.dto.ParticipantDeleteReq;
import com.linking.participant.dto.ParticipantRes;
import com.linking.participant.persistence.ParticipantMapper;
import com.linking.project.persistence.ProjectRepository;
import com.linking.project.domain.Project;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public Optional<ParticipantRes> createParticipant(ParticipantIdReq participantIdReq)
            throws NoResultException, DuplicateKeyException {
//        Optional<Participant> partData = participantRepository.findByUserEmail(participantCreateEmailReq.getEmail());
//        Optional<Participant> partData = participantRepository.findById(participantCreateEmailReq.get());
        List<Participant> partData = participantRepository.findByUserAndProject(
                participantIdReq.getUserId(), participantIdReq.getProjectId());
        if(!partData.isEmpty())
            throw new DuplicateKeyException("Duplicated user's email");
        Optional<User> userData = userRepository.findById(participantIdReq.getUserId());
        if(userData.isEmpty())
            throw new NoResultException();
        Optional<Project> projectData = projectRepository.findById(participantIdReq.getProjectId());
        if(projectData.isEmpty())
            throw new NoResultException();

        return Optional.of(participantMapper.toDto(
                participantRepository.save(
                        participantMapper.toEntity(
                                new ParticipantEntityReq(userData.get(), projectData.get())))));
    }

    public Optional<ParticipantRes> getParticipant(Long participantId) throws NoResultException{
        Optional<Participant> data = participantRepository.findById(participantId);
        if(data.isEmpty())
            throw new NoResultException();
        return Optional.of(participantMapper.toDto(data.get()));
    }

    public List<ParticipantRes> getParticipantsByProjectId(Long projectId) throws NoResultException{
        Optional<Project> projData = projectRepository.findById(projectId);
        if(projData.isEmpty())
            throw new NoResultException();
        List<Participant> data = participantRepository.findByProject(projData.get());
        if(data.isEmpty())
            throw new NoResultException();
        return participantMapper.toDto(data);
    }

    public Optional<ParticipantRes> deleteParticipant(ParticipantIdReq participantIdReq){
        List<Participant> data = participantRepository.findByUserAndProject(
                participantIdReq.getUserId(), participantIdReq.getProjectId());
        if(data.isEmpty())
            throw new NoResultException();
        participantRepository.delete(data.get(0));
        return Optional.of(participantMapper.toDto(data.get(0)));
    }

    public List<ParticipantRes> deleteParticipants(ParticipantDeleteReq participantDeleteReq)
            throws EmptyResultDataAccessException {
        List<Participant> data = participantRepository.findAllById(participantDeleteReq.getPartIdList());
        participantRepository.deleteAllById(participantDeleteReq.getPartIdList());
        return participantMapper.toDto(data);
    }

}
