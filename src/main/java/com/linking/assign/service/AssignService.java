package com.linking.assign.service;

import com.linking.assign.domain.Assign;
import com.linking.assign.domain.Status;
import com.linking.assign.dto.AssignCountRes;
import com.linking.assign.dto.AssignRes;
import com.linking.assign.dto.AssignStatusUpdateReq;
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
        List<AssignCountRes> countList =
                assignRepository.findCountByParticipantAndStatus(Status.COMPLETE, participantList);
        return assignMapper.toRatioDto(countList);
    }

    public Optional<AssignRes> updateAssignStatus(AssignStatusUpdateReq assignStatusUpdateReq){
        Optional<Assign> possibleAssign = assignRepository.findById(assignStatusUpdateReq.getAssignId());
        if(possibleAssign.isPresent()) {
            Assign.AssignBuilder assignBuilder = Assign.builder();
            assignBuilder
                    .assignId(assignStatusUpdateReq.getAssignId())
                    .todo(possibleAssign.get().getTodo())
                    .participant(possibleAssign.get().getParticipant())
                    .status(Status.valueOf(assignStatusUpdateReq.getStatus()));
            return Optional.ofNullable(assignMapper.toDto(assignRepository.save(assignBuilder.build())));
        }
        return Optional.empty();
    }

}
