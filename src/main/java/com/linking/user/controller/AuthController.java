package com.linking.user.controller;

import com.linking.global.ResponseHandler;
import com.linking.user.dto.UserEmailVerifyReq;
import com.linking.user.dto.UserSignUpReq;
import com.linking.user.service.UserService;
import com.linking.user.dto.UserSignInReq;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-up")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<Object> signUpDefault(@RequestBody @Valid UserSignUpReq userSignUpReq){
        try {
            return userService.addUser(userSignUpReq)
                    .map(ResponseHandler::generateOkResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
        } catch(DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/verify/email")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<Object> verifyEmail(@RequestBody @Valid UserEmailVerifyReq emailReq) {
        if(userService.isUniqueEmail(emailReq))
            return ResponseHandler.generateResponse("이미 존재하는 이메일", HttpStatus.OK, false);
        return ResponseHandler.generateResponse("고유한 이메일", HttpStatus.OK, true);
    }

    // TODO: login security needs to be upgraded
    @PostMapping("/sign-in")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<Object> signIn(@RequestBody @Valid UserSignInReq userSignInReq){
        try {
            return userService.getUserWithEmailAndPw(userSignInReq)
                    .map(ResponseHandler::generateOkResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
        } catch(NoSuchElementException e){
            return ResponseHandler.generateNotFoundResponse();
        }
    }

}
