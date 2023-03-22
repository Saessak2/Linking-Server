package com.linking.user.controller;

import com.linking.user.service.UserService;
import com.linking.user.dto.UserSignInDefaultReq;
import com.linking.user.dto.UserDetailedRes;
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
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-in/default")
    public ResponseEntity<UserDetailedRes> signInDefault(@RequestBody @Valid UserSignInDefaultReq userSignInDefaultReq){
        Optional<UserDetailedRes> result = userService.findUser(userSignInDefaultReq);
        return result
                .map(userDetailedResDto -> ResponseEntity.ok().body(userDetailedResDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
