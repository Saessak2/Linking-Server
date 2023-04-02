package com.linking.user.controller;

import com.linking.project.ProjectService;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.user.dto.UserEmailReq;
import com.linking.user.dto.UserEmailRes;
import com.linking.user.dto.UserIdReq;
import com.linking.user.service.UserService;
import com.linking.user.dto.UserDetailedRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE})
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;

    @PostMapping()
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<List<UserDetailedRes>> getUserList(
            @RequestParam("proj-id") Long projectId,
            @RequestBody UserEmailReq userEmailReq){
         List<UserDetailedRes> userData = userService.findUserByPartOfEmail(userEmailReq);
         if(projectId != -1) {
             Optional<ProjectContainsPartsRes> partData = projectService.getProject(projectId);
             if(partData.isPresent())
                 userData = userData.stream().filter(
                         u -> partData.get().getPartList().stream().noneMatch(
                                 p -> u.getUserId().equals(p.getUserId())))
                         .collect(Collectors.toList());
         }
         if(userData.isEmpty())
             return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
         return ResponseEntity.ok(userData);
    }

    @GetMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<UserDetailedRes> getUser(@RequestBody @Valid UserIdReq userIdReq){
        return userService.findUser(userIdReq)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.DELETE})
    public ResponseEntity<UserDetailedRes> deleteUser(@RequestBody @Valid UserIdReq userIdReq){
        return userService.deleteUser(userIdReq)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
