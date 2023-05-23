package com.linking.domain.assign.dto;

import com.linking.domain.participant.domain.Participant;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignCountRes {

    private Participant participant;
    private int count;
    private int completeCount;

    public AssignCountRes(Participant participant, Long count, Long completeCount){
        this.participant = participant;
        this.count = count.intValue();
        this.completeCount = completeCount.intValue();
    }

}
