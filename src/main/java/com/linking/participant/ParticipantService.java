package com.linking.participant;

import com.linking.participant.domain.Participant;
import com.linking.participant.dto.ParticipantCreateEmailReq;
import com.linking.participant.dto.ParticipantCreateReq;
import com.linking.participant.dto.ParticipantRes;
import com.linking.participant.mapper.ParticipantMapper;
import com.linking.project.ProjectRepository;
import com.linking.project.domain.Project;
import com.linking.user.User;
import com.linking.user.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public Optional<ParticipantRes> createParticipant(ParticipantCreateEmailReq participantCreateEmailReq)
            throws NoResultException{
        Optional<User> userData = userRepository.findByEmail(participantCreateEmailReq.getEmail());
        if(userData.isEmpty())
            throw new NoResultException();
        Optional<Project> projectData = projectRepository.findById(participantCreateEmailReq.getProjectId());
        if(projectData.isEmpty())
            throw new NoResultException();

        return Optional.of(participantMapper.toDto(
                participantRepository.save(
                        participantMapper.toEntity(
                                new ParticipantCreateReq(userData.get(), projectData.get())))));
    }

    public Optional<ParticipantRes> getParticipant(Long participantId) throws NoResultException{
        Optional<Participant> data = participantRepository.findById(participantId);
        if(data.isEmpty())
            throw new NoResultException();
        return Optional.of(participantMapper.toDto(data.get()));
    }

    public List<ParticipantRes> getParticipantsByProjectId(Long projectId) throws NoResultException{
        List<Participant> data = participantRepository.findByProject(projectId);
        if(data.isEmpty())
            throw new NoResultException();
        return participantMapper.toDto(data);
    }

    public Optional<ParticipantRes> deleteParticipant(Long participantId){
        Optional<Participant> data = participantRepository.findById(participantId);
        if(data.isEmpty())
            throw new NoResultException();
        participantRepository.deleteById(participantId);
        return Optional.of(participantMapper.toDto(data.get()));
    }

}
