package com.linking.domain.firebase_token.service;

import com.linking.domain.firebase_token.domain.FirebaseToken;
import com.linking.domain.firebase_token.dto.TokenReq;
import com.linking.domain.firebase_token.persistence.FirebaseTokenRepository;
import com.linking.domain.user.domain.User;
import com.linking.domain.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseTokenService {

    private final FirebaseTokenRepository firebaseTokenRepository;
    private final UserRepository userRepository;

    public void createFirebaseToken(Long userId) {
        User user = userRepository.getReferenceById(userId);

        FirebaseToken firebaseToken = FirebaseToken.builder()
                .user(user)
                .build();
        firebaseTokenRepository.save(firebaseToken);
    }

    @Transactional
    public boolean updateAppToken(TokenReq req) {
        FirebaseToken firebaseToken = firebaseTokenRepository.findByUserId(req.getUserId())
                .orElseThrow(NoSuchElementException::new);

        firebaseToken.setAppToken(req.getToken());

        return true;
    }

    @Transactional
    public boolean updateWebToken(TokenReq req) {
        FirebaseToken firebaseToken = firebaseTokenRepository.findByUserId(req.getUserId())
                .orElseThrow(NoSuchElementException::new);

        firebaseToken.setWebToken(req.getToken());

        return true;
    }


    // TODO timestamp 주기적으로 업뎃
}
