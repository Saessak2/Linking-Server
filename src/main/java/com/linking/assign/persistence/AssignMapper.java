package com.linking.assign.persistence;

import com.linking.assign.domain.Assign;
import com.linking.assign.dto.AssignCountReq;
import com.linking.assign.dto.AssignRatioRes;
import com.linking.assign.dto.AssignRes;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AssignMapper {

    default AssignRes toDto(Assign assign){
        if(assign == null)
            return null;

        AssignRes.AssignResBuilder assignResBuilder = AssignRes.builder();
        return assignResBuilder
                .assignId(assign.getAssignId())
                .userId(assign.getParticipant().getUser().getUserId())
                .userName(assign.getParticipant().getUserName())
                .status(String.valueOf(assign.getStatus())).build();
    }

    List<AssignRes> toDto(List<Assign> assignList);

    default AssignRatioRes toDto(AssignCountReq totalReq, AssignCountReq completeReq){
        if(totalReq == null || completeReq == null)
            return null;

        AssignRatioRes.AssignRatioResBuilder assignRatioResBuilder = AssignRatioRes.builder();
        return assignRatioResBuilder
                .participantId(totalReq.getParticipantId())
                .totalAssign(totalReq.getCount())
                .completeAssign(completeReq.getCount())
                .completionRatio((double) completeReq.getCount() / totalReq.getCount() * 100).build();
    }

    default List<AssignRatioRes> toDto(List<AssignCountReq> totalCountList, List<AssignCountReq> completeCountList){
        if(totalCountList == null || completeCountList == null)
            return null;

        List<AssignRatioRes> assignRatioList = new ArrayList<>();
        for(int i = 0; i < totalCountList.size(); i++)
            assignRatioList.add(toDto(totalCountList.get(i), completeCountList.get(i)));

        return assignRatioList;
    }

}
