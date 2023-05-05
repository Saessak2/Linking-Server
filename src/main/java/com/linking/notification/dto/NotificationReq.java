package com.linking.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReq {

    private Long projectId;
    private Long receiverId;
    private String sender; // 보낸사람 이름
    private String priority;
    private String type;
    private Long targetId; // 페이지인 경우에만
}
