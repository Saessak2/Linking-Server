package com.linking.project;

import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectContainsPartsRes;
import com.linking.project.dto.ProjectUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<ProjectContainsPartsRes> postProject(
            @RequestBody @Valid ProjectCreateReq projectCreateReq){
        try {
            return projectService.createProject(projectCreateReq)
                    .map(projectContainsPartsRes -> ResponseEntity.status(HttpStatus.CREATED).body(projectContainsPartsRes))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<ProjectContainsPartsRes> getProject(
            @RequestParam("id") Long projectId){
        try {
            return projectService.getProject(projectId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/list")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<List<ProjectContainsPartsRes>> getProjectList(
            @RequestParam("user-id") Long userId){
        try {
            List<ProjectContainsPartsRes> projectList = projectService.getProjectsByOwnerId(userId);
            if(projectList.isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return ResponseEntity.ok(projectList);
        } catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.PUT})
    public ResponseEntity<ProjectContainsPartsRes> putProject(
            @RequestBody @Valid ProjectUpdateReq projectUpdateReq){
        try {
            return projectService.updateProject(projectUpdateReq)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.DELETE})
    public ResponseEntity<ProjectContainsPartsRes> deleteProject(
            @RequestParam("id") Long projectId){
        try{
            return projectService.deleteProject(projectId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch(SQLIntegrityConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
