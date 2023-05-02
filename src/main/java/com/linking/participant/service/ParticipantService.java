package com.linking.participant.service;

import com.linking.pageCheck.service.PageCheckService;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.dto.ParticipantIdReq;
import com.linking.participant.dto.ParticipantDeleteReq;
import com.linking.participant.dto.ParticipantRes;
import com.linking.participant.persistence.ParticipantMapper;
import com.linking.project.domain.Project;
import com.linking.project.dto.ProjectRes;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.project.persistence.ProjectMapper;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final PageCheckService pageCheckService;
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    public Optional<ParticipantRes> createParticipant(ParticipantIdReq participantIdReq)
            throws DataIntegrityViolationException {
        List<Participant> partData = participantRepository.findByUserAndProject(
                participantIdReq.getUserId(), participantIdReq.getProjectId());
        if (!partData.isEmpty())
            throw new DuplicateKeyException("Already in project");

        Participant participant = participantRepository.save(participantMapper.toEntity(participantIdReq));
        pageCheckService.createPageCheck(participant);

        return Optional.of(participantMapper.toDto(participant));
    }

    public Optional<ParticipantRes> getParticipant(Long participantId)
            throws NoSuchElementException {
        return Optional.ofNullable(participantRepository.findById(participantId)
                .map(participantMapper::toDto)
                .orElseThrow(NoSuchElementException::new));
    }

    public List<ProjectRes> getPartsByUserId(Long userId)
            throws NoSuchElementException {
        List<Project> projectList = participantRepository.findProjectsByUser(userId);
        if (projectList.isEmpty())
            throw new NoSuchElementException();
        return projectMapper.toDto(projectList);
    }

    public List<Long> updateParticipantList(ProjectUpdateReq projectUpdateReq)
        throws DataIntegrityViolationException {
        List<Participant> curPartList =
                participantRepository.findByProject(new Project(projectUpdateReq.getProjectId()));
        List<Long> partUserIdList = curPartList.stream()
                .map(p->p.getUser().getUserId()).collect(Collectors.toList());
        List<Long> reqPartUserList = projectUpdateReq.getPartList();
        List<Long> resPartIdList = new ArrayList<>();

        if(!curPartList.get(0).getUser().getUserId().equals(reqPartUserList.get(0)))
            throw new DataIntegrityViolationException("삭제할 수 없는 팀원");

        Participant.ParticipantBuilder participantBuilder = Participant.builder();
        for(int i = 0, skippedIndex; i < reqPartUserList.size(); i++){
            skippedIndex = partUserIdList.indexOf(reqPartUserList.get(i));
            if(skippedIndex == -1 || curPartList.isEmpty()){
                User user = userRepository.findById(reqPartUserList.get(i))
                        .orElseThrow(NoSuchElementException::new);
                participantBuilder
                        .project(new Project(projectUpdateReq.getProjectId()))
                        .user(user)
                        .userName(user.getFullName());
                resPartIdList.add(
                        participantRepository.save(participantBuilder.build()).getParticipantId());
            }
            else{
                resPartIdList.add(curPartList.get(skippedIndex).getParticipantId());
                curPartList.remove(skippedIndex);
            }
        }
        participantRepository.deleteAll(curPartList);
        return resPartIdList;
    }

    public void deleteParticipant(ParticipantDeleteReq participantDeleteReq)
            throws NoSuchElementException, DataIntegrityViolationException {
        List<Participant> participantList = setParticipantList(participantDeleteReq.getPartIdList());
        if(participantList.isEmpty())
            throw new NoSuchElementException();
        if(containsOwner(participantList)) {
            throw new DataIntegrityViolationException("삭제할 수 없는 팀원");
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
}
