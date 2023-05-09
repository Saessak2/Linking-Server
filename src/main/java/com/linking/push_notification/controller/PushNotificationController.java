package com.linking.push_notification.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.push_notification.dto.PushNotificationRes;
import com.linking.push_notification.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/push-notifications")
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;

    // todo 알림함 조회
    @GetMapping("/{userId}")
    public ResponseEntity getAllNotifications(@PathVariable("userId") Long userId) {

        List<PushNotificationRes> res = pushNotificationService.findAllNotificationsByUser(userId);
        return ResponseHandler.generateOkResponse(res);
    }

    // todo 알림 전송
}
