package com.linking.push_notification.service;

import com.linking.push_notification.domain.PushNotification;
import com.linking.push_notification.dto.PushNotificationRes;
import com.linking.push_notification.persistence.PushNotificationRepository;
import com.linking.push_settings.persistence.PushSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushNotificationRepository pushNotificationRepository;
    private final PushSettingsRepository pushSettingsRepository;

    // todo 알림함 조회
    public List<PushNotificationRes> findAllNotificationsByUser(Long userId) {

        List<PushNotification> notifications = pushNotificationRepository.findAllByUserId(userId);

        PushNotificationRes.PushNotificationResBuilder builder = PushNotificationRes.builder();
        List<PushNotificationRes> resList = new ArrayList<>();

        for (PushNotification not : notifications) {
            builder
                    .projectId(not.getProject().getProjectId())
                    .body(not.getBody())
                    .info(not.getInfo())
                    .priority(not.getPriority())
                    .noticeType(not.getNoticeType())
                    .isChecked(not.isChecked())
                    .targetId(not.getTargetId());

            resList.add(builder.build());
        }
        return resList;
    }


    // todo 알림 전송

    // todo 알림 한달마다 삭제
}
