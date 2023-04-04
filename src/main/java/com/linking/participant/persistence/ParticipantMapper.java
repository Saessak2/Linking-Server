package com.linking.participant.persistence;

import com.linking.participant.domain.Participant;
import com.linking.participant.dto.ParticipantEntityReq;
import com.linking.participant.dto.ParticipantRes;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    default ParticipantRes toDto(Participant participant) {
        if(participant == null)
            return null;

        ParticipantRes.ParticipantResBuilder partResBuilder = ParticipantRes.builder();
        partResBuilder
                .participantId(participant.getParticipantId())
                .userId(participant.getUser().getUserId())
                .projectId(participant.getProject().getProjectId());

        return partResBuilder.build();
    }

    default List<ParticipantRes> toDto(List<Participant> partList) {
        if(partList.isEmpty())
            return null;
        return partList.stream().map(this::toDto).collect(Collectors.toList());
    }

    default Participant toEntity(ParticipantEntityReq participantEntityReq){
        if(participantEntityReq == null)
            return null;

        Participant.ParticipantBuilder partBuilder = Participant.builder();
        partBuilder
                .user(participantEntityReq.getUser())
                .project(participantEntityReq.getProject());

        return partBuilder.build();
    }

}


