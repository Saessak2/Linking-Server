package com.linking.firebase_token.controller;

import com.linking.firebase_token.service.FirebaseTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/fcm-token")
public class FirebaseTokenController {

    private final FirebaseTokenService firebaseTokenService;

    // TODO app token 요청
    public void putAppToken() {

    }

    // TODO web token 요청
    public void putWebToken() {

    }
}
