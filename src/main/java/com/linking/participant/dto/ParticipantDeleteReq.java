package com.linking.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantDeleteReq {

    List<Long> partIdList;

}
