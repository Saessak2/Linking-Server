package com.linking.domain.user.controller;

import com.linking.global.common.Login;
import com.linking.global.common.ResponseHandler;
import com.linking.global.common.UserCheck;
import com.linking.domain.user.dto.UserEmailReq;
import com.linking.domain.user.dto.UserEmailRes;
import com.linking.domain.user.service.UserService;
import com.linking.domain.user.dto.UserDetailedRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/email")
    public ResponseEntity<Object> getUserListWithEmail(
            @RequestBody UserEmailReq userEmailReq,
            @Login UserCheck userCheck
    ){
        List<UserDetailedRes> userList = userService.getUsersByPartOfEmail(userEmailReq);
        if(userList.isEmpty())
            return ResponseHandler.generateResponse(
                    ResponseHandler.MSG_404, HttpStatus.NOT_FOUND, new UserEmailRes(Boolean.FALSE, null));
        return ResponseHandler.generateResponse(
                ResponseHandler.MSG_200, HttpStatus.OK, new UserEmailRes(Boolean.TRUE, userList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(
            @PathVariable Long id,
            @Login UserCheck userCheck
    ){
        return userService.getUserById(id)
                .map(u -> ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, u))
                .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
    }

    // TODO: 프로젝트를 소유하는 회원이면 소유자를 이전함( -> project service)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(
            @PathVariable Long id,
            @Login UserCheck userCheck
    ){
        userService.deleteUser(id);
        return ResponseHandler.generateNoContentResponse();
    }

}
