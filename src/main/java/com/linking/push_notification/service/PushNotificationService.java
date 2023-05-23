package com.linking.push_notification.service;

import com.linking.firebase_token.domain.FirebaseToken;
import com.linking.firebase_token.persistence.FirebaseTokenRepository;
import com.linking.page.persistence.PageRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import com.linking.push_notification.domain.NoticePriority;
import com.linking.push_notification.domain.PushNotification;
import com.linking.push_notification.dto.FcmReq;
import com.linking.push_notification.dto.PushNotificationReq;
import com.linking.push_notification.dto.PushNotificationRes;
import com.linking.push_notification.persistence.PushNotificationRepository;
import com.linking.push_settings.domain.PushSettings;
import com.linking.push_settings.persistence.PushSettingsRepository;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushNotificationRepository pushNotificationRepository;
    private final PushSettingsRepository pushSettingsRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final EmailService emailService;
    private final FcmService fcmService;
    private final FirebaseTokenRepository firebaseTokenRepository;
    private final PageRepository pageRepository;

    @Transactional
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
                    .isChecked(not.isChecked());
            if (not.getTargetId() != null) {
                builder
                        .targetId(not.getTargetId())
                        .assistantId(pageRepository.getGroupIdByPageId(not.getTargetId()));
            } else {
                builder
                        .targetId(-1L)
                        .assistantId(-1L);
            }
            resList.add(builder.build());
            not.setChecked(true);
        }
        return resList;
    }

    // todo 알림 한달마다 삭제

    // todo 알림 전송
    public boolean sendPushNotification(PushNotificationReq req) {

        PushNotification pushNotification = this.createPushNotification(req);
        PushSettings settings = pushSettingsRepository.findByUserId(pushNotification.getUser().getUserId())
                .orElseThrow(NoSuchElementException::new);

        // 메일 전송
        if (req.getPriority() == NoticePriority.ALL && settings.isAllowedMail())
            emailService.sendEmail(pushNotification);

        // 푸시 알림 전송
        if (settings.isAllowedWebPush() || settings.isAllowedAppPush()) {

            FcmReq.FcmReqBuilder fcmReqBuilder = FcmReq.builder();
            fcmReqBuilder
                    .title(pushNotification.getProject().getProjectName())
                    .body(pushNotification.getSender() + "\n" + pushNotification.getBody());

            Map<String, String> data = new HashMap<>();

            if (settings.isAllowedWebPush()) {

                FirebaseToken firebaseToken = firebaseTokenRepository.findByUserId(pushNotification.getUser().getUserId())
                        .orElseThrow(NoSuchElementException::new);

                fcmReqBuilder
                        .firebaseToken(firebaseToken.getWebToken())
                        .link("https://github.com/Saessak2/Linking-Server");

                fcmService.sendWebMessageToFcmServer(fcmReqBuilder.build());
            }

            if (settings.isAllowedAppPush()) {

//                data.put("projectId", String.valueOf(pushNotification.getProject().getProjectId()));
//                data.put("type", String.valueOf(pushNotification.getNoticeType()));
//                data.put("targetId", String.valueOf(pushNotification.getTargetId()));

                FirebaseToken firebaseToken = firebaseTokenRepository.findByUserId(pushNotification.getUser().getUserId())
                        .orElseThrow(NoSuchElementException::new);

                fcmReqBuilder
                        .firebaseToken(firebaseToken.getAppToken())
                        .link("https://github.com/Saessak2/Linking-Server");

                fcmService.sendWebMessageToFcmServer(fcmReqBuilder.build());
            }
        }
        return true;
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
}
