package com.linking.user.controller;

import com.linking.project.service.ProjectService;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.user.dto.UserEmailReq;
import com.linking.user.dto.UserEmailRes;
import com.linking.user.service.UserService;
import com.linking.user.dto.UserDetailedRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
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
    public ResponseEntity<UserEmailRes> getUserListWithEmail(
            @RequestParam("proj-id") Long projectId,
            @RequestBody UserEmailReq userEmailReq){
        try {

            List<UserDetailedRes> userData = userService.findUserByPartOfEmail(userEmailReq);
            if (projectId != -1 && !userData.isEmpty()) {
                Optional<ProjectContainsPartsRes> partData = projectService.getProjectsContainingParts(projectId);
                if (partData.isPresent())
                    userData = userData.stream().filter(
                                    u -> partData.get().getPartList().stream().noneMatch(
                                            p -> u.getUserId().equals(p.getUserId())))
                            .collect(Collectors.toList());
            }
            if (userData.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserEmailRes(Boolean.FALSE, null));
            return ResponseEntity.ok(new UserEmailRes(Boolean.TRUE, userData));
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserEmailRes(Boolean.FALSE, null));
        }
    }

    @GetMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<UserDetailedRes> getUser(@RequestParam("id") Long userId){
        return userService.findUser(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.DELETE})
    public ResponseEntity<UserDetailedRes> deleteUser(@RequestParam("id") Long userId){
        return userService.deleteUser(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
