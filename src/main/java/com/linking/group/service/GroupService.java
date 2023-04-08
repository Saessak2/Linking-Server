package com.linking.group.service;

import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupUpdateTitleReq;
import com.linking.group.persistence.GroupMapper;
import com.linking.group.persistence.GroupRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ProjectRepository projectRepository;


    // TODO check duplicated docIndex
    public GroupRes createGroup(GroupCreateReq groupCreateReq) throws NoSuchElementException{
        Project refProject = projectRepository.getReferenceById(groupCreateReq.getProjectId());

        Group group = groupMapper.toEntity(groupCreateReq);
        group.setProject(refProject);

        return groupMapper.toDto(groupRepository.save(group));
    }

    public void updateGroup(GroupUpdateTitleReq req) throws NoSuchElementException{
        Group findGroup = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        if (!findGroup.getName().equals(req.getName())) {
            findGroup.updateName(req.getName());
            groupRepository.save(findGroup);
        }
    }

    public void deleteGroup(Long groupId) throws NoSuchElementException{

        // TODO 프로젝트의 document 리스트에서 삭제 되는지 확인 필요함

        groupRepository.delete(
                groupRepository.findById(groupId).orElseThrow(
                        () -> new NoSuchElementException(ErrorMessage.NO_GROUP)
                )
        );
    }
}
