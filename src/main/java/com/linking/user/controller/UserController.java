package com.linking.user.controller;

import com.linking.user.UserService;
import com.linking.user.dto.UserResDto;
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
    public ResponseEntity<UserResDto> signUpDefault(@RequestBody @Valid UserSignUpDefaultReq userSignUpDefaultReq){
        Optional<UserResDto> resData = userService.addUser(userSignUpDefaultReq);
        return resData
                .map(userResDto -> ResponseEntity.ok().body(userResDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
