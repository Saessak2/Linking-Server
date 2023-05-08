package com.linking.notification.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.notification.dto.NotificationReq;
import com.linking.notification.dto.NotificationRes;
import com.linking.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Object> getNotifications(
            @RequestParam(value = "userId") Long userId
    ) {
        log.info("{} \ngetNotifications", this.getClass().getSimpleName());

        List<NotificationRes> res = notificationService.findAllNotificationsByReceiver(userId);
        return ResponseHandler.generateOkResponse(res);
    }

    @PostMapping
    public ResponseEntity<Object> postNotification(
            @RequestBody @Valid NotificationReq notificationReq
    ) {
        boolean res = notificationService.createNotification(notificationReq);
        return ResponseHandler.generateCreatedResponse(res);
    }
}
