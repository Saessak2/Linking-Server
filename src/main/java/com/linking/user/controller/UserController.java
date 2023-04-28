package com.linking.user.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.user.dto.UserEmailReq;
import com.linking.user.dto.UserEmailRes;
import com.linking.user.service.UserService;
import com.linking.user.dto.UserDetailedRes;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/email")
    public ResponseEntity<Object> getUserListWithEmail(@RequestBody UserEmailReq userEmailReq){
        try {
            List<UserDetailedRes> userList = userService.getUsersByPartOfEmail(userEmailReq);
            return ResponseHandler.generateOkResponse(new UserEmailRes(Boolean.TRUE, userList));
        } catch(NoSuchElementException e){
            return ResponseHandler.generateResponse(
                    ResponseHandler.MSG_404, HttpStatus.NOT_FOUND, new UserEmailRes(Boolean.FALSE, null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Long userId){
        try {
            return userService.getUserById(userId)
                    .map(ResponseHandler::generateOkResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
        } catch (NoSuchElementException e){
            return ResponseHandler.generateNotFoundResponse();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseHandler.generateNoContentResponse();
        } catch(EmptyResultDataAccessException e){
            return ResponseHandler.generateNotFoundResponse();
        } catch(DataIntegrityViolationException e){
            return ResponseHandler.generateBadRequestResponse();
        }
    }
}
