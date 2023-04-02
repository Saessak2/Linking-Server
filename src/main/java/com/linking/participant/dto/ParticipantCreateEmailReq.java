package com.linking.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantCreateEmailReq {

    @NotNull
    @Email
    private String email;

    @NotNull
    private Long projectId;

}
