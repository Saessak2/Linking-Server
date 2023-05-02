package com.linking.assign.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignCountRes {

    private Long participantId;
    private int count;
    private int completeCount;

    public AssignCountRes(Long participantId, Long count, Long completeCount){
        this.participantId = participantId;
        this.count = count.intValue();
        this.completeCount = completeCount.intValue();
    }

}
