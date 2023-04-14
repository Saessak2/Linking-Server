package com.linking.group.service;

import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.dto.*;
import com.linking.group.persistence.GroupMapper;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageOrderReq;
import com.linking.page.dto.PageRes;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final ApplicationEventPublisher publisher;
    Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ProjectRepository projectRepository;
    private final PageRepository pageRepository;
    private final PageMapper pageMapper;

    // 그룹 리스트 조회
    public List<GroupRes> findAllGroups(Long projectId)  {

        List<GroupRes> groupResList = new ArrayList<>();

        List<Group> groupList = groupRepository.findAllByProjectId(projectId);
        for (Group group : groupList) {
            List<PageRes> pageResList = new ArrayList<>();

            List<Page> pageList = group.getPageList();
            if (!pageList.isEmpty())
                pageList.forEach(p -> {pageResList.add(pageMapper.toDto(p));});
            groupResList.add(groupMapper.toDto(group, pageResList));
        }
        return groupResList;
    }

    public Optional<GroupRes> createGroup(GroupCreateReq req) {
        Project refProject = projectRepository.getReferenceById(req.getProjectId());
        Group group = groupMapper.toEntity(req);
        group.setProject(refProject);
        GroupRes groupRes = groupMapper.toDto(groupRepository.save(group), new ArrayList<>());

        // 이벤트 발행
//        if (groupRes != null) {
//            publisher.publishEvent(groupRes);
//            logger.info("GroupCreate is published");
//        }

        return Optional.ofNullable(groupRes);
    }

    // 순서 변경 (그룹 + 페이지)
    public void updateDocumentsOrder(List<GroupOrderReq> groupOrderReqList) {

        // 그룹 순서 변경
        List<Long> groupIds = groupOrderReqList.stream()
                .map(GroupOrderReq::getGroupId)
                .collect(Collectors.toList());

        Long projectId = null;
        int temp = 0;

        for (Group g : groupRepository.findAllById(groupIds)) {
            if (temp == 0) {projectId = g.getProject().getProjectId(); temp++;}
            // 요청 온 순서대로 order 지정
            int order = groupIds.indexOf(g.getId());
            if (g.getGroupOrder() != order) {
                g.updateOrder(order);
                groupRepository.save(g);
            }
        }
        // 페이지 순서 변경
        for (GroupOrderReq groupOrderReq : groupOrderReqList) {
            List<Long> pageIds = groupOrderReq.getPageList().stream()
                    .map(PageOrderReq::getPageId)
                    .collect(Collectors.toList());

            for (Page p : pageRepository.findAllById(pageIds)) {
                int order = pageIds.indexOf(p.getId());
                if (p.getPageOrder() != order) {
                    p.updateOrder(order);
                    pageRepository.save(p);
                }
            }
        }

        // 이벤트 발행
//        publisher.publishEvent(this.findAllGroups(projectId));

    }

    public void updateGroupName(GroupNameReq req) throws NoSuchElementException{
        Group findGroup = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        if (!findGroup.getName().equals(req.getName())) {
            findGroup.updateName(req.getName());
            Group group = groupRepository.save(findGroup);
            // 이벤트 발행
//            publisher.publishEvent(groupMapper.toDto(group, new ArrayList<>()));
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

        // 이벤트 발행
//        publisher.publishEvent();
    }
}
