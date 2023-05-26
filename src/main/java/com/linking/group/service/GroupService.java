package com.linking.group.service;

import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Template;
import com.linking.page_check.domain.PageCheck;
import com.linking.page_check.persistence.PageCheckRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import com.linking.global.message.ErrorMessage;
import com.linking.sse.EventType;
import com.linking.sse.event.GroupEvent;
import com.linking.group.dto.*;
import com.linking.group.persistence.GroupMapper;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageOrderReq;
import com.linking.page.dto.PageRes;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final ApplicationEventPublisher publisher;
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ProjectRepository projectRepository;
    private final PageRepository pageRepository;
    private final PageMapper pageMapper;
    private final PageCheckRepository pageCheckRepository;

    // 그룹 리스트 조회
    public List<GroupRes> findAllGroups(Long projectId, Long userId)  {

        List<Group> groupList = groupRepository.findAllByProjectId(projectId);
        if (groupList.isEmpty()) return new ArrayList<>();

        List<PageCheck> pageCheckList = pageCheckRepository.findAllByParticipant(userId, projectId);

        Map<Long, Integer> annoNotCnts = new HashMap<>(); // key -> pageId
        pageCheckList.forEach(pc -> {
            annoNotCnts.put(pc.getPage().getId(), pc.getAnnoNotCount());
        });

        List<GroupRes> groupDetailedResList = new ArrayList<>();
        for (Group group : groupList) {  // order 순서
            List<PageRes> pageResList = new ArrayList<>();
            List<Page> pageList = group.getPageList();   // order 순서

            if (!pageList.isEmpty()) {
                pageList.forEach(p -> {
                    pageResList.add(pageMapper.toDto(p, annoNotCnts.get(p.getId())));
                });
            }
            groupDetailedResList.add(groupMapper.toDto(group, pageResList));
        }
        return groupDetailedResList;
    }

    public GroupRes createGroup(GroupCreateReq req, Long userId) {
        Project project = projectRepository.findById(req.getProjectId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PROJECT));

        Group group = groupMapper.toEntity(req);
        group.setProject(project);
        GroupRes groupRes = groupMapper.toDto(groupRepository.save(group), new ArrayList<>());

        publisher.publishEvent(GroupEvent.builder()
                .eventName(EventType.POST_GROUP)
                .projectId(project.getProjectId())
                .userId(userId)
                .data(GroupRes.builder()
                        .groupId(groupRes.getGroupId())
                        .name(group.getName())
                        .build())
                .build());

        return groupRes;
    }

    public Boolean updateGroupName(GroupNameReq req, Long userId) {

        Group findGroup = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        if (!findGroup.getName().equals(req.getName())) {
            findGroup.updateName(req.getName());
            Group group = groupRepository.save(findGroup);

            publisher.publishEvent(GroupEvent.builder()
                    .eventName(EventType.PUT_GROUP_NAME)
                    .projectId(findGroup.getProject().getProjectId())
                    .userId(userId)
                    .data(GroupRes.builder()
                            .groupId(group.getId())
                            .name(group.getName())
                            .build())
                    .build());
        }
        return true;
    }

    // 순서 변경 (그룹 + 페이지)
    public boolean updateDocumentsOrder(List<GroupOrderReq> groupOrderReqList) {

        // 그룹 순서 변경
        List<Long> groupIds = groupOrderReqList.stream()
                .map(GroupOrderReq::getGroupId)
                .collect(Collectors.toList());

        List<Group> groups = groupRepository.findAllById(groupIds);
        Long projectId = null;
        if (!groups.isEmpty())
            projectId = groups.get(0).getProject().getProjectId();

        for (Group g : groups) {
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
        return true;
    }



    public void deleteGroup(Long groupId, Long userId) throws NoSuchElementException{

        Group group =  groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));
        Long projectId = group.getProject().getProjectId();
        groupRepository.delete(group);

        publisher.publishEvent(GroupEvent.builder()
                .eventName(EventType.DELETE_GROUP)
                .projectId(projectId)
                .userId(userId)
                .data(GroupRes.builder().groupId(groupId).build())
                .build());

        // 그룹 순서를 0부터 재정렬
        List<Group> groupList = groupRepository.findAllByProjectId(projectId);
        int order = 0;
        for (Group g : groupList) {
            if (g.getGroupOrder() != order) {
                g.updateOrder(order);
                groupRepository.save(g);
            }
            order++;
        }
    }

    public List<GroupRes> getBlockPages(Long projectId) {

        List<Group> groups = groupRepository.findAllByProjectId(projectId);
        if (groups == null) return new ArrayList<>();

        List<GroupRes> groupResList = new ArrayList<>();

        for (Group group : groups) {

            List<PageRes> pageResList = new ArrayList<>();
            List<Page> pageList = group.getPageList();

            boolean flag = false;
            if (!pageList.isEmpty()) {
                for (Page page : pageList) {
                    if (page.getTemplate() == Template.BLOCK) {
                        flag = true;
                        pageResList.add(
                                PageRes.builder()
                                        .pageId(page.getId())
                                        .title(page.getTitle())
                                        .build());
                    }
                }
                if (flag) {
                    groupResList.add(GroupRes.builder()
                            .groupId(group.getId())
                            .name(group.getName())
                            .pageResList(pageResList)
                            .build());
                }
            }
        }
        return groupResList;
    }
}
