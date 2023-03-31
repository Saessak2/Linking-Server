package com.linking.project;

import com.linking.project.dto.ProjectCreateReq;
import com.linking.project.dto.ProjectRes;
import com.linking.project.dto.ProjectUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping()
    public ResponseEntity<ProjectRes> postProject(@RequestBody @Valid ProjectCreateReq projectCreateReq){
        return projectService.createProject(projectCreateReq)
                .map(projectRes -> ResponseEntity.status(HttpStatus.CREATED).body(projectRes))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/project-id={project-id}")
    public ResponseEntity<ProjectRes> getProject(@PathVariable("project-id") Long projectId){
        try {
            return projectService.getProject(projectId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/list/user-id={user-id}")
    public ResponseEntity<List<ProjectRes>> getProjectList(@PathVariable("user-id") Long userId){
        try {
            List<ProjectRes> projectList = projectService.getProjectByOwnerId(userId);
            if(projectList.isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return ResponseEntity.ok(projectList);
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping
    public ResponseEntity<ProjectRes> putProject(@RequestBody @Valid ProjectUpdateReq projectUpdateReq){
        return projectService.updateProject(projectUpdateReq)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @DeleteMapping("/project-id={project-id}")
    public ResponseEntity<ProjectRes> deleteProject(@PathVariable("project-id") Long projectId){
        try{
            return projectService.deleteProject(projectId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
