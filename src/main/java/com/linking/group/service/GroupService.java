package com.linking.group.service;

import com.linking.document.dto.DocumentEvent;
import com.linking.document.service.DocumentService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher publisher;
    private final DocumentService documentService;
    Logger logger = LoggerFactory.getLogger(GroupService.class);

    public Optional<GroupRes> createGroup(GroupCreateReq req) {
        Project refProject = projectRepository.getReferenceById(req.getProjectId());
        Group group = groupMapper.toEntity(req);
        group.setProject(refProject);
        GroupRes groupRes = groupMapper.toDto(groupRepository.save(group));

        if (groupRes != null) {
            publisher.publishEvent(
                    new DocumentEvent(
                            refProject.getProjectId(),
                            documentService.findAllDocuments(refProject.getProjectId()))
            );
            logger.info("create group publisher");
        }

        return Optional.ofNullable(groupRes);
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
        try {
            List<Group> groupList = groupRepository.findAllByProjectId(projectId);
            int order = 0;
            for (Group g : groupList) {
                if (g.getGroupOrder() != order) {
                    g.updateOrder(order);
                    groupRepository.save(g);
                }
                order++;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
