package com.linking.push_settings.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.push_settings.dto.PushSettingsUpdateReq;
import com.linking.push_settings.service.PushSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/push-settings")
public class PushSettingsController {

    private final PushSettingsService pushSettingsService;

    @PutMapping("/app")
    public ResponseEntity<Object> putAppSettings(@RequestBody @Valid PushSettingsUpdateReq req) {
        boolean res = pushSettingsService.updateAppSettings(req);
        return ResponseHandler.generateOkResponse(res);
    }

    @PutMapping("/web")
    public ResponseEntity<Object> putWebSettings(@RequestBody @Valid PushSettingsUpdateReq req) {
        boolean res = pushSettingsService.updateWebSettings(req);
        return ResponseHandler.generateOkResponse(res);
    }
}
