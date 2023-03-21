package com.linking.user.controller;

import com.linking.user.service.UserService;
import com.linking.user.dto.UserSignInDefaultReq;
import com.linking.user.dto.UserRes;
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
    public ResponseEntity<UserRes> signInDefault(@RequestBody @Valid UserSignInDefaultReq userSignInDefaultReq){
        Optional<UserRes> result = userService.findUser(userSignInDefaultReq);
        return result
                .map(userResDto -> ResponseEntity.ok().body(userResDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
