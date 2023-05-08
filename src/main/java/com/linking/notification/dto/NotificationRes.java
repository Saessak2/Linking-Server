package com.linking.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRes {

    private Long projectId;
    private String title;
    private String info; // projectName + sender + createdDate
    private String priority;
    private String type; // 알림 타입(할일 or 페이지)
    private Long targetId;
}