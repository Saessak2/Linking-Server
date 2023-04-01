package com.linking.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantRes {

    private Long participantId;
    private Long userId;
    private Long projectId;

}
