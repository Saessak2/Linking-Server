package com.linking.user.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.global.common.SessionConst;
import com.linking.user.dto.UserDetailedRes;
import com.linking.user.dto.UserSignInReq;
import com.linking.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(
            @RequestBody @Valid UserSignInReq req,
            HttpSession session
    ) {
        UserDetailedRes res = null;
        try {
            res = userService.getUserWithEmailAndPw(req).get();
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateNotFoundResponse();
        }

//        UUID uid = Optional.ofNullable(UUID.class.cast(session.getAttribute("uid")))
//                  .orElse(UUID.randomUUID());
        session.setAttribute(SessionConst.LOGIN_USER, res.getUserId());

        return ResponseHandler.generateOkResponse(res);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpSession session) {
        session.invalidate();
        return ResponseHandler.generateOkResponse("로그아웃 성공");
    }
}
