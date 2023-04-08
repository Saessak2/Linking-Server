package com.linking.group.controller;

import com.linking.global.ResponseHandler;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupUpdateTitleReq;
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
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.PUT})
public class GroupController {

    private final GroupService groupService;


    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<Object> postGroup(@RequestBody @Valid GroupCreateReq req) {

        try {
            GroupRes groupRes = groupService.createGroup(req);
            if (groupRes == null)
                return ResponseHandler.generateInternalServerErrorResponse();
            return ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, groupRes);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }


    @PutMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.PUT})
    public ResponseEntity<Object> putGroup(@RequestBody @Valid GroupUpdateTitleReq req) {

        try {
            groupService.updateGroup(req);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, true);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }


    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.DELETE})
    public ResponseEntity<Object> deleteGroup(@PathVariable("id") Long groupId) {

        try {
            groupService.deleteGroup(groupId);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_204, HttpStatus.NO_CONTENT, null);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }
}
