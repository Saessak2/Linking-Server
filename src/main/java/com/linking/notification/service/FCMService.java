package com.linking.notification.service;

import com.google.firebase.messaging.*;
import com.linking.global.message.ErrorMessage;
import com.linking.notification.dto.FCMReq;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public void sendNotificationByToken(FCMReq req) {

        User user = userRepository.findById(req.getTargetUserId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_USER));

        if (user.getFcmToken() == null) {
            log.info("유저의 firebase Token이 존재하지 않습니다. targetUserId = {}", req.getTargetUserId());
            return;
        }

        Notification notification = Notification.builder()
                .setTitle(req.getTitle())
                .setBody(req.getBody())
                .build();

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder().setBadge(42).build())
                .putHeader("apns-priority", "5")
                .build();

        WebpushConfig webpushConfig = WebpushConfig.builder()
                .putHeader("Urgency", "normal")
                .build();

//        FcmOptions fcmOptions = FcmOptions.builder()
//                .setAnalyticsLabel()
//                .build();
//
        Message message = Message.builder()
                .setToken(user.getFcmToken())
                .setNotification(notification)
                .putAllData(req.getData())
//                .setFcmOptions()
                .build();

        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully sent message: {}", response);

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            log.info("Error sending message", req.getTargetUserId());
        }
    }
}
