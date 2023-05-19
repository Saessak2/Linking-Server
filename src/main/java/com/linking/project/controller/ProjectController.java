package com.linking.project.controller;

import com.linking.global.common.Login;
import com.linking.global.common.ResponseHandler;
import com.linking.global.common.UserCheck;
import com.linking.group.controller.GroupSseHandler;
import com.linking.participant.service.ParticipantService;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.service.ProjectService;
import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectUpdateReq;
import com.linking.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final GroupSseHandler groupSseHandler;
    private final ProjectService projectService;
    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<Object> postProject(
            @RequestBody @Valid ProjectCreateReq projectCreateReq,
            @Login UserCheck userCheck
    ) {
        return projectService.createProject(projectCreateReq)
                .map(ResponseHandler::generateCreatedResponse)
                .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProject(
            @PathVariable Long id,
            @Login UserCheck userCheck
    ) {
        return projectService.getProjectsContainingParts(id)
                .map(p -> ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, p))
                .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
    }

    @GetMapping("/list/owner/{id}")
    public ResponseEntity<Object> getProjectListByOwner(
            @PathVariable Long id,
            @Login UserCheck userCheck
    ){
        List<ProjectContainsPartsRes> projectList = projectService.getProjectsByOwnerId(id);
        if(projectList.isEmpty())
            return ResponseHandler.generateInternalServerErrorResponse();
        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, projectList);
    }

    @GetMapping("/list/part/{id}")
    public ResponseEntity<Object> getProjectListByPart(
            @PathVariable Long id,
            @Login UserCheck userCheck
    ){
        List<ProjectContainsPartsRes> projectList = projectService.getProjectsByUserId(id);
        if(projectList.isEmpty())
            return ResponseHandler.generateInternalServerErrorResponse();
        return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, projectList);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Object> putProject(
            @RequestBody @Valid ProjectUpdateReq projectUpdateReq,
            @Login UserCheck userCheck
    ){
        return projectService.updateProject(projectUpdateReq, participantService.updateParticipantList(projectUpdateReq))
                .map(p -> ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, p))
                .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProject(
            @PathVariable Long id,
            @Login UserCheck userCheck
    ){
        projectService.deleteProject(id);
        groupSseHandler.removeEmittersByProject(id);
        return ResponseHandler.generateNoContentResponse();
    }

}
