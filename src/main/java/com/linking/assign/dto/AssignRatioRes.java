package com.linking.assign.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignRatioRes {

    private Long participantId;
    private int totalAssign;
    private int completeAssign;
    private double completionRatio;

}
