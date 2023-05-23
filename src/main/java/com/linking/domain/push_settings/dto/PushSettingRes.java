package com.linking.domain.push_settings.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushSettingRes {

    private boolean allowedWebAppPush;
    private boolean allowedMail;
}
