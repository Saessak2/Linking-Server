package com.linking.push_notification.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.push_notification.dto.PushNotificationReq;
import com.linking.push_notification.dto.PushNotificationRes;
import com.linking.push_notification.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/push-notifications")
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;

    @GetMapping("/{userId}")
    public ResponseEntity getAllNotifications(@PathVariable("userId") Long userId) {

        List<PushNotificationRes> res = pushNotificationService.findAllPushNotificationsByUser(userId);
        return ResponseHandler.generateOkResponse(res);
    }

    // todo 알림 전송
    @PostMapping
    public void postPushNotification(@RequestBody @Valid PushNotificationReq req) {
        pushNotificationService.sendPushNotification(req);
    }
}
