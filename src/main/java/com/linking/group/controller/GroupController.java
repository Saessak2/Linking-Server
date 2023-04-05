package com.linking.group.controller;

import com.linking.global.ErrorMessage;
import com.linking.global.ResponseHandler;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupUpdateReq;
import com.linking.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;


    @PostMapping
    public ResponseEntity<Object> postGroup(@RequestBody @Valid GroupCreateReq groupCreateReq) {

        try {
            GroupRes groupRes = groupService.createGroup(groupCreateReq);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, groupRes);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(ErrorMessage.ERROR, HttpStatus.BAD_REQUEST, null);
        }
    }


    @PutMapping
    public ResponseEntity<Object> putGroup(@RequestBody @Valid GroupUpdateReq groupUpdateReq) {

        try {
            GroupRes groupRes = groupService.updateGroup(groupUpdateReq);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, groupRes);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteGroup(@RequestParam("id") Long groupId) {

        if (groupId == null) {
            return ResponseHandler.generateResponse(ResponseHandler.MSG_400, HttpStatus.BAD_REQUEST, null);
        }
        try {
            groupService.deleteGroup(groupId);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }
}
