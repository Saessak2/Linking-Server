package com.linking.notification.service;

import com.linking.notification.domain.Notification;
import com.linking.notification.dto.NotificationRes;
import com.linking.notification.persistence.NotificationRepository;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

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
