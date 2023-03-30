package com.linking.group;

import com.linking.group.dto.GroupReqDto;
import com.linking.project.Project;
import com.linking.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProjectRepository projectRepository;

    public void createGroup(GroupReqDto groupReqDto) throws NoSuchElementException{
        Project project = projectRepository.findById(groupReqDto.getProjectDto().getId())
                .orElseThrow(() -> new NoSuchElementException());

        Group group = Group.builder()
                .name(groupReqDto.getName())
                .doc_depth(groupReqDto.getDocDepth())
                .doc_index(groupReqDto.getDocIndex())
                .project(project)
                .build();

        groupRepository.save(group);
    }
}
