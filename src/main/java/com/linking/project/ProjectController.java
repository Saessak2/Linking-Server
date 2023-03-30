package com.linking.project;

import com.linking.project.dto.ProjectParamDto;
import com.linking.project.dto.ProjectReqDto;
import com.linking.project.dto.ProjectResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name = "/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProject(@RequestBody ProjectReqDto projectReqDto) {
        projectService.createProject(projectReqDto);
    }

    @GetMapping
    public ProjectResDto getProject(@RequestParam ProjectParamDto projectParamDto) {
        return projectService.findProject(projectParamDto);
    }
}
