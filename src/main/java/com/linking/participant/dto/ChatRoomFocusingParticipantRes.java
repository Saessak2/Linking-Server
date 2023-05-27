package com.linking.participant.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomFocusingParticipantRes {

    private Long userId;//tobedeleted
    private String userName;

}
