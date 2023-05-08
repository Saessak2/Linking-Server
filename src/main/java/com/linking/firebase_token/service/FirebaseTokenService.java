package com.linking.firebase_token.service;

import com.linking.firebase_token.dto.TokenReq;
import com.linking.firebase_token.persistence.FirebaseTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseTokenService {

    private FirebaseTokenRepository firebaseTokenRepository;

    public void updateAppToken(TokenReq req) {

    }

    public void updateWebToken(TokenReq req) {
    }


    // TODO timestamp 주기적으로 업뎃
}
