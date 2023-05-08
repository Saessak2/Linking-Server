package com.linking.notification.service;

import com.linking.notification.domain.Notification;
import com.linking.notification.dto.FCMReq;
import com.linking.notification.dto.NotificationReq;
import com.linking.notification.dto.NotificationRes;
import com.linking.notification.persistence.NotificationRepository;
import com.linking.page.persistence.PageRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import com.linking.todo.persistence.TodoRepository;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PageRepository pageRepository;
    private final TodoRepository todoRepository;
    private final FCMService fcmService;

    // 알림 전송
    public boolean createNotification(NotificationReq req) {

        Project project = projectRepository.getReferenceById(req.getProjectId());
        User user = userRepository.getReferenceById(req.getReceiverId());

        Notification.NotificationBuilder notificationBuilder = Notification.builder();
        notificationBuilder
                .project(project)
                .user(user)
                .sender(req.getSender())
                .priority(req.getPriority())
                .type(req.getType());

        if (req.getType().equals("PAGE")) {
            notificationBuilder
                    .targetId(req.getTargetId())
                    .body(req.getBody());

        } else if (req.getType().equals("TODO")) {
            notificationBuilder.body(req.getBody());
        }

        Notification notification = notificationRepository.save(notificationBuilder.build());


        if (req.getPriority().equals("ALL")) {
            // 알림 허용 여부 확인
            sendMail();
        }
        // 알림 허용 여부 확인
        sendFCMMessage(notification);

        return true;
    }


    public void sendFCMMessage(Notification notification) {
        // fcm 알림 전송
        FCMReq.FCMReqBuilder fcmReqBuilder = FCMReq.builder();

        fcmReqBuilder
                .targetUserId(notification.getUser().getUserId())
                .title(notification.getProject().getProjectName())
                .body(notification.getSender() + "\n" + notification.getBody());

        Map<String, String> data = new HashMap<>();
        data.put("projectId", String.valueOf(notification.getProject().getProjectId()));
        data.put("type", notification.getType());
        data.put("targetId", String.valueOf(notification.getTargetId()));

        fcmReqBuilder
                .data(data);

        fcmService.sendNotificationByToken(fcmReqBuilder.build());
    }

    public void sendMail() {
    }

    // 알림함 조회
    public List<NotificationRes> findAllNotificationsByReceiver(Long userId) {

        log.info("{} \nfindAllNotificationsByReceiver", this.getClass().getSimpleName());

        User user = userRepository.getReferenceById(userId);
        List<Notification> notifications = notificationRepository.findAllByUser(user);

        List<NotificationRes> resList = new ArrayList<>();
        NotificationRes.NotificationResBuilder builder = NotificationRes.builder();
        for (Notification not : notifications) {
            builder
                    .projectId(not.getProject().getProjectId())
                    .title("?")
                    .info(not.getInfo())
                    .priority(not.getPriority())
                    .type(not.getType());

            if(not.getType().equals("PAGE"))
                builder.targetId(not.getTargetId());
            else if (not.getType().equals("TODO"))
                builder.targetId(-1L);

            resList.add(builder.build());
        }
        return resList;
    }

    // 알림 매달 삭제 - 나중에

}
