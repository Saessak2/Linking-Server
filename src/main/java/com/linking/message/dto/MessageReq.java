package com.linking.message.dto;

import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReq {

    @NotNull
    private Long userId;

    @NotNull
    private Long projectId;

    @NotNull
    private String content;

    @NotNull
    private String sentDatetime;

    @Enumerated(value = EnumType.STRING)
    private MessageType messageType;

}
