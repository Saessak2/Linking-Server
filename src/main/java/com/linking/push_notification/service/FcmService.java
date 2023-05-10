package com.linking.push_notification.service;

import com.google.firebase.messaging.*;
import com.linking.push_notification.dto.FcmReq;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;

    @Async("eventCallExecutor")
    public void sendMessageToFcmServer(FcmReq req) {

        Notification notification = Notification.builder()
                .setTitle(req.getTitle())
                .setBody(req.getBody())
                .build();

        ApnsFcmOptions apnsFcmOptions = ApnsFcmOptions.builder()
                .setImage("")
                .build();

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder().setBadge(42).build())
                .putHeader("apns-priority", "5")
                .setFcmOptions(apnsFcmOptions)
                .build();

        WebpushConfig webpushConfig = WebpushConfig.builder()
                .putHeader("Urgency", "normal")
                .putHeader("image", "")
                .build();
//
//        FcmOptions fcmOptions = FcmOptions.builder()
//                .setAnalyticsLabel()
//                .build();

        Message message = Message.builder()
                .setToken(req.getFirebaseToken())
                .setNotification(notification)
                .putAllData(req.getData())
                .setApnsConfig(apnsConfig)
                .setWebpushConfig(webpushConfig)
//                .setFcmOptions()
                .build();

        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully send message: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
            log.info("Error sending message");
        }
    }
}
