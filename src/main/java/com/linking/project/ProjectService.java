package com.linking.project;

import com.linking.project.dto.ProjectParamDto;
import com.linking.project.dto.ProjectReqDto;
import com.linking.project.dto.ProjectResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public void createProject(ProjectReqDto projectReqDto) {
        Project project = Project.builder()
                .documentList(new ArrayList<>())
                .build();

        projectRepository.save(project);
    }

    public ProjectResDto findProject(ProjectParamDto projectParamDto) throws NoSuchElementException{
        Project findProject = projectRepository.findById(projectParamDto.getId())
                .orElseThrow(() -> new NoSuchElementException());

        ProjectResDto projectResDto = ProjectResDto.builder()
                .id(findProject.getId())
                .build();

        return projectResDto;
    }
}
