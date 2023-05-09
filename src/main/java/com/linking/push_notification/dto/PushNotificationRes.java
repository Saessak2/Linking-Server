package com.linking.push_notification.dto;

import com.linking.push_notification.domain.NoticeType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PushNotificationRes {

    private Long projectId;
    private String body;  // 페이지 / 할일 title
    private String info;  // projectName + sender + createdDate
    private int priority;   // 0 / 1
    private NoticeType noticeType; // TODO / PAGE
    private boolean isChecked;
    private Long targetId; // pageId
}
