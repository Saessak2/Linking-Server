package com.linking.group.service;

import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupUpdateNameReq;
import com.linking.group.persistence.GroupMapper;
import com.linking.group.persistence.GroupRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ProjectRepository projectRepository;

    public Optional<GroupRes> createGroup(GroupCreateReq req) throws NoSuchElementException{
        Project refProject = projectRepository.getReferenceById(req.getProjectId());

        Group group = groupMapper.toEntity(req);
        group.setProject(refProject);

        return Optional.ofNullable(groupMapper.toDto(groupRepository.save(group)));
    }

    public void updateGroupName(GroupUpdateNameReq req) throws NoSuchElementException{
        Group findGroup = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        if (!findGroup.getName().equals(req.getName())) {
            findGroup.updateName(req.getName());
            groupRepository.save(findGroup);
        }
    }

    public void deleteGroup(Long groupId) throws NoSuchElementException{
        Group group =  groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        Long projectId = group.getProject().getProjectId();
        groupRepository.delete(group);

        List<Group> groupList = groupRepository.findAllByProjectId(projectId);
        updateOrder(groupList);
    }

    private void updateOrder(List<Group> groupList) {
        int idx = 0;
        for (Group group : groupList) {
            group.updateOrder(idx++);
        }
        groupRepository.saveAll(groupList);
    }
}
