package com.linking.push_settings.service;

import com.linking.push_settings.domain.PushSettings;
import com.linking.push_settings.dto.PushSettingsUpdateReq;
import com.linking.push_settings.persistence.PushSettingsRepository;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushSettingsService {

    private final PushSettingsRepository pushSettingsRepository;
    private final UserRepository userRepository;

    public void createPushSettings(Long userId) {
        User user = userRepository.getReferenceById(userId);

        PushSettings pushSettings = PushSettings.builder()
                .user(user)
                .build();
        pushSettingsRepository.save(pushSettings);
    }

    @Transactional
    public boolean updateAppSettings(PushSettingsUpdateReq req) {
        PushSettings pushSettings = pushSettingsRepository.findByUserId(req.getUserId())
                .orElseThrow(NoSuchElementException::new);
        pushSettings.setAppSettings(req.isAllowedWebAppPush(), req.isAllowedMail()); // 엡 푸시 설정

        return true;
    }

    @Transactional
    public boolean updateWebSettings(PushSettingsUpdateReq req) {
        PushSettings pushSettings = pushSettingsRepository.findByUserId(req.getUserId())
                .orElseThrow(NoSuchElementException::new);
        pushSettings.setWepSettings(req.isAllowedWebAppPush(), req.isAllowedMail()); // 웹 푸시 설정

        return true;
    }
}
