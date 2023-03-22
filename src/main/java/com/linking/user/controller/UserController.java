package com.linking.user.controller;

import com.linking.user.dto.UserEmailVerifyReq;
import com.linking.user.service.UserService;
import com.linking.user.dto.UserDetailedRes;
import com.linking.user.dto.UserSignUpDefaultReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up/default")
    public ResponseEntity<UserDetailedRes> signUpDefault(@RequestBody @Valid UserSignUpDefaultReq userSignUpDefaultReq){
        Optional<UserDetailedRes> resData = userService.addUser(userSignUpDefaultReq);
        return resData
                .map(userDetailedResDto -> ResponseEntity.ok().body(userDetailedResDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PostMapping("/sign-up/email-verify")
    public ResponseEntity<Boolean> emailVerify(@RequestBody @Valid UserEmailVerifyReq emailReq) {
        if(userService.findDuplicatedEmail(emailReq))
            return ResponseEntity.ok(true);
        return ResponseEntity.ok(false);

//        return ResponseEntity.of(
//                Optional.of(
//                        new UserEmailVerifyRes(
//                                userService.findDuplicatedEmail(emailReq))));
    }

}
