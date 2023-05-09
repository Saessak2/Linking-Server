package com.linking.push_notification.service;

import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import com.linking.push_notification.domain.PushNotification;
import com.linking.push_notification.dto.PushNotificationReq;
import com.linking.push_notification.dto.PushNotificationRes;
import com.linking.push_notification.persistence.PushNotificationRepository;
import com.linking.push_settings.persistence.PushSettingsRepository;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
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
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public List<PushNotificationRes> findAllPushNotificationsByUser(Long userId) {

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
                    .targetId(not.getTargetId()); //todo 할일인 경우에 null?

            resList.add(builder.build());
        }
        return resList;
    }

    // todo 알림 한달마다 삭제

    // todo 알림 전송
    public void sendPushNotification(PushNotificationReq req) {

        PushNotification pushNotification = this.createPushNotification(req);

    }

    public PushNotification createPushNotification(PushNotificationReq req) {

        User user = userRepository.getReferenceById(req.getUserId());
        Project project = projectRepository.getReferenceById(req.getProjectId());

        PushNotification pushNotification = PushNotification.builder()
                .user(user)
                .project(project)
                .targetId(req.getTargetId())
                .sender(req.getSender())
                .noticeType(req.getNoticeType())
                .priority(req.getPriority())
                .body(req.getBody())
                .build();

        return pushNotificationRepository.save(pushNotification);
    }

    // todo fcmService 로 알림 건내기
    // todo mailService 로 알림 건내기

}
