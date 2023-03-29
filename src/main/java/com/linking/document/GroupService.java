package com.linking.document;

import com.linking.document.dto.GroupDto;
import com.linking.project.Project;
import com.linking.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProjectRepository projectRepository;
    public void createNewGroup(GroupDto groupDto) {
        // TODO 예외처리
        Optional<Project> projectOptional = projectRepository.findById(groupDto.getProjectDto().getId());
        Project project = projectOptional.get();

        groupRepository.save(group);
    }
}
