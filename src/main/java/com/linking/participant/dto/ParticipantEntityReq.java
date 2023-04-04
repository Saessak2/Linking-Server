package com.linking.participant.dto;

import com.linking.project.domain.Project;
import com.linking.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantEntityReq {

    private User user;
    private Project project;

}
