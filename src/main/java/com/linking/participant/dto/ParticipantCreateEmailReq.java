package com.linking.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantCreateEmailReq {

    @NotNull
    @Email
    private String email;

    @NotNull
    private Long projectId;

}
