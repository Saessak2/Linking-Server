package com.linking.push_notification.service;

import com.linking.push_notification.persistence.PushNotificationRepository;
import com.linking.push_settings.persistence.PushSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final PushSettingsRepository pushSettingsRepository;
    private final PushNotificationRepository pushNotificationRepository;

    // todo 메일 전송

}
