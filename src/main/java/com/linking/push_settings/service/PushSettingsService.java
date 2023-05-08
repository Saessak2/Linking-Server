package com.linking.push_settings.service;

import com.linking.push_settings.domain.PushSettings;
import com.linking.push_settings.persistence.PushSettingsRepository;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    }
}
