package com.linking.project;

import com.linking.document.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;

    public Optional<Project> findProject(Long projectId) {
        return projectRepository.findById(projectId);
    }
}
