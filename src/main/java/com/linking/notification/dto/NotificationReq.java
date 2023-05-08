package com.linking.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReq {

    @NotNull
    private Long projectId;
    @NotNull
    private Long receiverId;
    @NotNull
    private String sender; // 보낸사람 이름
    @NotNull
    private String priority;
    @NotNull
    private String type;
    private Long targetId; // 페이지인 경우에만
    @NotNull
    private String body; // 페이지의 title or 할일의 content
}
