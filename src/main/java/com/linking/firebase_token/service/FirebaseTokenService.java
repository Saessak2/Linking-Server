package com.linking.firebase_token.service;

import com.linking.firebase_token.persistence.FirebaseTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseTokenService {

    private FirebaseTokenRepository firebaseTokenRepository;

    // TODO app token 초기화
    // TODO web token 초기화
    // TODO timestamp 주기적으로 업뎃
}
