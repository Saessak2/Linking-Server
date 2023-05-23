package com.linking.domain.participant.dto;

import com.linking.domain.project.domain.Project;
import com.linking.domain.user.domain.User;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantEntityReq {

    private User user;
    private Project project;

}
