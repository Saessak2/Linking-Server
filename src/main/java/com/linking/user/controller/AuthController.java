package com.linking.user.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.user.dto.UserEmailVerifyReq;
import com.linking.user.dto.UserFcmTokenReq;
import com.linking.user.dto.UserSignUpReq;
import com.linking.user.service.UserService;
import com.linking.user.dto.UserSignInReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUpDefault(@RequestBody @Valid UserSignUpReq userSignUpReq){
        return userService.addUser(userSignUpReq)
                .map(u -> ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, u))
                .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
    }

    @PostMapping("/verify/email")
    public ResponseEntity<Object> verifyEmail(@RequestBody @Valid UserEmailVerifyReq emailReq) {
        if(userService.isUniqueEmail(emailReq))
            return ResponseHandler.generateResponse("이미 존재하는 이메일", HttpStatus.OK, false);
        return ResponseHandler.generateResponse("고유한 이메일", HttpStatus.OK, true);
    }

    // TODO: login security needs to be upgraded
    @PostMapping("/sign-in")
    public ResponseEntity<Object> signIn(@RequestBody @Valid UserSignInReq userSignInReq){
        return userService.getUserWithEmailAndPw(userSignInReq)
                .map(u -> ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, u))
                .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
    }
}
