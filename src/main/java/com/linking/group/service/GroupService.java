package com.linking.group.service;

import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupOrderReq;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ProjectRepository projectRepository;
    private int GROUP_FIRST_ORDER = 0;

    public Optional<GroupRes> createGroup(GroupCreateReq req) throws NoSuchElementException{
        // TODO project 존재 여부 확인
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
        // 그룹 순서를 0부터 재정렬
        List<Group> groupList = groupRepository.findAllByProjectId(projectId);
        int order = GROUP_FIRST_ORDER;
        for (Group g : groupList) {
            if (g.getGroupOrder() != order) {
                g.updateOrder(order++);
                groupRepository.save(g);
            }
            order++;
        }
    }

    public void updateOrder(List<GroupOrderReq> reqs) throws RuntimeException {

        List<Long> groupIds = reqs.stream()
                .map(GroupOrderReq::getGroupId)
                .collect(Collectors.toList());

        List<Group> groupList = groupRepository.findAllById(groupIds);
        for (Group g : groupList) {
            int order = groupIds.indexOf(g.getId());
            if (g.getGroupOrder() != order) {
                g.updateOrder(order);
                groupRepository.save(g);
            }
        }
    }
}
