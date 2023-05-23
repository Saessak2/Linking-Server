package com.linking.domain.participant.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantSimplifiedRes {

    private Long userId;
    private String userName;

}
