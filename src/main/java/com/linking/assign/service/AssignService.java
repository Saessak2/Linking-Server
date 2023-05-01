package com.linking.assign.service;

import com.linking.assign.domain.Assign;
import com.linking.assign.domain.Status;
import com.linking.assign.dto.AssignCountReq;
import com.linking.assign.persistence.AssignMapper;
import com.linking.assign.persistence.AssignRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.project.domain.Project;
import com.linking.assign.dto.AssignRatioRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignService {

    private final AssignRepository assignRepository;
    private final AssignMapper assignMapper;

    private final ParticipantRepository participantRepository;

    public List<AssignRatioRes> getAssignCompletionRate(Long id) {
        List<Participant> participantList = participantRepository.findByProject(new Project(id));
        List<AssignCountReq> totalCountList = assignRepository.findCountByParticipant(participantList);
        List<AssignCountReq> completeCountList =
                assignRepository.findCountByParticipantAndStatus(participantList, String.valueOf(Status.COMPLETE));
        return assignMapper.toDto(totalCountList, completeCountList);
    }

    public Optional<Long> updateAssignStatus(Long id, String status){
        Optional<Assign> possibleAssign = assignRepository.findById(id);
        if(possibleAssign.isPresent()) {
            Assign.AssignBuilder assignBuilder = Assign.builder();
            assignBuilder
                    .assignId(id)
                    .todo(possibleAssign.get().getTodo())
                    .participant(possibleAssign.get().getParticipant())
                    .status(Status.valueOf(status));
            return Optional.ofNullable(assignRepository.save(assignBuilder.build()).getAssignId());
        }
        return Optional.empty();
    }

}
