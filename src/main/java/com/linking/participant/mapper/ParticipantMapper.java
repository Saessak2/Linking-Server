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

        ParticipantRes.ParticipantResBuilder participantRes = ParticipantRes.builder();
        participantRes.participantId(participant.getParticipantId());
        participantRes.user(participant.getUser());
        participantRes.project(participant.getProject());

        return participantRes.build();
    }

    default List<ParticipantRes> toDto(List<Participant> partList) {
        if(partList.isEmpty())
            return null;
        return partList.stream().map(this::toDto).collect(Collectors.toList());
    }

    default Participant toEntity(ParticipantCreateReq participantCreateReq){
        if(participantCreateReq == null)
            return null;

        Participant participant = new Participant();
        participant.setUser(participantCreateReq.getUser());
        participant.setProject(participantCreateReq.getProject());

        return participant;
    }

}


