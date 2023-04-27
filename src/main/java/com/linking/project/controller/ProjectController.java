package com.linking.project.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.project.service.ProjectService;
import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.dto.ProjectUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Object> postProject(
            @RequestBody @Valid ProjectCreateReq projectCreateReq){
        try {
            return projectService.createProject(projectCreateReq)
                    .map(ResponseHandler::generateCreatedResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
        } catch(DataIntegrityViolationException e){
            return ResponseHandler.generateNotFoundResponse();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> getProject(
            @PathVariable("id") Long projectId){
        try {
            return projectService.getProjectsContainingParts(projectId)
                    .map(ResponseHandler::generateOkResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
        } catch(NoSuchElementException e){
            return ResponseHandler.generateNotFoundResponse();
        }
    }

    @PostMapping("/list/{id}")
    public ResponseEntity<Object> getProjectList(
            @PathVariable("id") Long userId){
        try {
            List<ProjectContainsPartsRes> projectList = projectService.getProjectsByOwnerId(userId);
            if(projectList.isEmpty())
                return ResponseHandler.generateInternalServerErrorResponse();
            return ResponseHandler.generateOkResponse(projectList);
        } catch(NoSuchElementException e){
            return ResponseHandler.generateNotFoundResponse();
        }
    }

    // TODO: participant create/delete process (After project update, cascade)
    @PutMapping
    public ResponseEntity<Object> putProject(
            @RequestBody @Valid ProjectUpdateReq projectUpdateReq){
        try {
            return projectService.updateProject(projectUpdateReq)
                    .map(ResponseHandler::generateOkResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
        } catch(NoSuchElementException e){
            return ResponseHandler.generateNotFoundResponse();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProject(
            @PathVariable("id") Long projectId){
        try{
            projectService.deleteProject(projectId);
            return ResponseHandler.generateNoContentResponse();
        } catch(EmptyResultDataAccessException e){
            return ResponseHandler.generateNotFoundResponse();
        } catch(SQLIntegrityConstraintViolationException e){
            return ResponseHandler.generateBadRequestResponse();
        }
    }

}
