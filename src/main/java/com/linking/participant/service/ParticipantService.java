package com.linking.participant.service;

import com.linking.page_check.service.PageCheckService;
import com.linking.participant.dto.ParticipantSimplifiedRes;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.dto.ParticipantIdReq;
import com.linking.participant.dto.ParticipantDeleteReq;
import com.linking.participant.dto.ParticipantRes;
import com.linking.participant.persistence.ParticipantMapper;
import com.linking.project.domain.Project;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.project.persistence.ProjectRepository;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final PageCheckService pageCheckService;
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

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

    public List<ParticipantSimplifiedRes> getParticipantsByProjectId(Long projectId)
            throws NoSuchElementException {
        List<Participant> participantList = participantRepository.findByProject(new Project(projectId));
        if (participantList.isEmpty())
            throw new NoSuchElementException();
        return participantMapper.toSimpleDto(participantList);
    }

    public List<Long> updateParticipantList(ProjectUpdateReq projectUpdateReq)
        throws DataIntegrityViolationException {
        List<Long> resPartIdList = new ArrayList<>();
        if(!projectUpdateReq.getIsPartListChanged()){
            for(Long id : projectUpdateReq.getPartList())
                resPartIdList.add(participantRepository.findByUserAndProjectId(id, projectUpdateReq.getProjectId())
                        .orElseThrow(NoSuchElementException::new).getParticipantId());
            return resPartIdList;
        }

        Project project = projectRepository.findById(projectUpdateReq.getProjectId())
                .orElseThrow(NoSuchElementException::new);
        List<Participant> curPartList =
                participantRepository.findByProject(project);
        List<Long> partUserIdList = curPartList.stream()
                .map(p->p.getUser().getUserId()).collect(Collectors.toList());
        List<Long> reqPartUserList = projectUpdateReq.getPartList();


        if(!project.getOwner().getUserId().equals(reqPartUserList.get(0)))
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

        Long ownerId = project.getOwner().getUserId();
        for(int i = 0; i < resPartIdList.size(); i++) {
            if (resPartIdList.get(i).equals(ownerId))
                Collections.swap(resPartIdList, 0, i);
        }
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
