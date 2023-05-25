package com.linking.firebase_token.controller;

import com.linking.firebase_token.dto.TokenReq;
import com.linking.firebase_token.service.FirebaseTokenService;
import com.linking.global.auth.Login;
import com.linking.global.auth.UserCheck;
import com.linking.global.common.ResponseHandler;
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
@RequestMapping(value = "/fcm-token")
public class FirebaseTokenController {

    private final FirebaseTokenService firebaseTokenService;

    // TODO app token 요청
    @PutMapping("/app")
    public ResponseEntity putAppFcmToken(
            @RequestBody @Valid TokenReq req,
            @Login UserCheck userCheck
    ) {
        return ResponseHandler.generateOkResponse(firebaseTokenService.updateAppToken(req));
    }

    // TODO web token 요청
    @PutMapping("/web")
    public ResponseEntity putWebFcmToken(
            @RequestBody @Valid TokenReq req,
            @Login UserCheck userCheck
    ) {
        return ResponseHandler.generateOkResponse(firebaseTokenService.updateWebToken(req));
    }
}
