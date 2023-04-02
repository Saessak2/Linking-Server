package com.linking.participant.mapper;

import com.linking.participant.domain.Participant;
import com.linking.participant.dto.ParticipantCreateReq;
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

    default Participant toEntity(ParticipantCreateReq participantCreateReq){
        if(participantCreateReq == null)
            return null;

        Participant.ParticipantBuilder partBuilder = Participant.builder();
        partBuilder
                .user(participantCreateReq.getUser())
                .project(participantCreateReq.getProject());

        return partBuilder.build();
    }

}


