package com.linking.participant.dto;

import com.linking.project.domain.Project;
import com.linking.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantRes {

    private Long participantId;
    private User user;
    private Project project;

}
