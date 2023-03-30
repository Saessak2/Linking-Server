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


}
