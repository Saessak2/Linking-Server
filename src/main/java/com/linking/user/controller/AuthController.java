package com.linking.user.controller;

import com.linking.user.dto.UserEmailVerifyReq;
import com.linking.user.dto.UserSignUpDefaultReq;
import com.linking.user.service.UserService;
import com.linking.user.dto.UserSignInDefaultReq;
import com.linking.user.dto.UserDetailedRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
public class AuthController {

    private final UserService userService;

    // TODO: Exception throw ** duplicated email(mysql exception -> 400) ** others 500
    @PostMapping("/sign-up/default")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<UserDetailedRes> signUpDefault(@RequestBody @Valid UserSignUpDefaultReq userSignUpDefaultReq){
        try {
            return userService.addUser(userSignUpDefaultReq)
                    .map(userDetailedResDto -> ResponseEntity.ok().body(userDetailedResDto))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(SQLIntegrityConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/sign-up/email-verify")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<Boolean> emailVerify(@RequestBody @Valid UserEmailVerifyReq emailReq) {
        if(userService.findDuplicatedEmail(emailReq))
            return ResponseEntity.ok(true);
        return ResponseEntity.ok(false);
    }

    @PostMapping("/sign-up/email-verify2")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<Boolean> emailVerify(@RequestBody @Valid UserSignUpDefaultReq emailReq) {
        if(userService.findDuplicatedEmail(emailReq))
            return ResponseEntity.ok(true);
        return ResponseEntity.ok(false);
    }

    // TODO: login security needs to be upgraded
    @PostMapping("/sign-in/default")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<UserDetailedRes> signInDefault(@RequestBody @Valid UserSignInDefaultReq userSignInDefaultReq){
        return userService.findUser(userSignInDefaultReq)
                .map(userDetailedResDto -> ResponseEntity.ok().body(userDetailedResDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
