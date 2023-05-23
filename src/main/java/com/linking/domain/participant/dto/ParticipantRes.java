package com.linking.domain.participant.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantRes {

    private Long participantId;
    private Long userId;
    private Long projectId;

}
