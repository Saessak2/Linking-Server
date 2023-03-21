package com.linking.user.controller;

import com.linking.user.UserService;
import com.linking.user.dto.UserSignInDefaultReq;
import com.linking.user.dto.UserResDto;
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
    public ResponseEntity<UserResDto> signInDefault(@RequestBody @Valid UserSignInDefaultReq userSignInDefaultReq){
        Optional<UserResDto> result = userService.findUser(userSignInDefaultReq);
        return result
                .map(userResDto -> ResponseEntity.ok().body(userResDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
