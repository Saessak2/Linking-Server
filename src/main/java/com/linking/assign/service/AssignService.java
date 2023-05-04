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
import com.linking.todo.domain.Todo;
import com.linking.todo.dto.TodoUpdateReq;
import com.linking.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
            return Optional.ofNullable(assignMapper.toResDto(assignRepository.save(assignBuilder.build())));
        }
        return Optional.empty();
    }

    public List<Long> updateAssignList(TodoUpdateReq todoUpdateReq){
        List<Long> resAssignList = new ArrayList<>();
        List<Assign> curAssignList = assignRepository.findByTodo(new Todo(todoUpdateReq.getTodoId()));
        List<Long> curPartIdList = curAssignList.stream()
                .map(a -> a.getParticipant().getParticipantId()).collect(Collectors.toList());
        List<Long> reqPartIdList = new ArrayList<>();
        for(Long id : todoUpdateReq.getAssignList())
            reqPartIdList.add(
                    participantRepository.findByUserAndProjectId(id, todoUpdateReq.getProjectId())
                            .orElseThrow(NoSuchElementException::new).getParticipantId());

        Assign.AssignBuilder assignBuilder = Assign.builder();
        for(int i = 0, skippedIndex; i < reqPartIdList.size(); i++){
            skippedIndex = curPartIdList.indexOf(reqPartIdList.get(i));
            if(skippedIndex == -1 || curAssignList.isEmpty()){
                Participant participant = participantRepository.findById(reqPartIdList.get(i))
                        .orElseThrow(NoSuchElementException::new);
                assignBuilder
                        .todo(new Todo(todoUpdateReq.getTodoId()))
                        .participant(participant)
                        .status(Status.BEFORE_START);
                resAssignList.add(
                        assignRepository.save(assignBuilder.build()).getAssignId());
            }
            else{
                resAssignList.add(curAssignList.get(skippedIndex).getAssignId());
                curAssignList.remove(skippedIndex);
            }
        }
        assignRepository.deleteAll(curAssignList);
        return resAssignList;
    }

}
