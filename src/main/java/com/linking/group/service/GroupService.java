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
import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.persistence.PageCheckRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import com.linking.ws.code.WsResType;
import com.linking.ws.event.DocumentEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    Logger logger = LoggerFactory.getLogger(GroupService.class);
    private final ApplicationEventPublisher publisher;

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ProjectRepository projectRepository;
    private final PageRepository pageRepository;
    private final PageMapper pageMapper;
    private final ParticipantRepository participantRepository;
    private final PageCheckRepository pageCheckRepository;

    DocumentEvent.DocumentEventBuilder docEvent = DocumentEvent.builder();

    // 그룹 리스트 조회
    public List<GroupRes> findAllGroups(Long projectId, Long userId)  {

        List<GroupRes> groupResList = new ArrayList<>();
        // page에서 pageCheckList를 가져올수 있지만 모든 팀원의 데이터를 들고 오기 떄문에 참여자로 pageCheckList를 들고옴
        Participant participant = participantRepository.findByUserAndProjectId(userId, projectId)
                .orElseThrow(() -> new NoSuchElementException());
        List<PageCheck> pageCheckList = pageCheckRepository.findAllAByParticipantId(participant.getParticipantId());
        Map<Long, Integer> annoNotiCnts = new HashMap<>(); // key -> pageId
        pageCheckList.forEach(pageCheck -> {
            annoNotiCnts.put(pageCheck.getPage().getId(), pageCheck.getAnnotNotiCnt());
        });

        List<Group> groupList = groupRepository.findAllByProjectId(projectId);
        for (Group group : groupList) {  // order 순
            List<PageRes> pageResList = new ArrayList<>();

            List<Page> pageList = group.getPageList();   // order순
            if (!pageList.isEmpty())
                pageList.forEach(p -> {
                    pageResList.add(pageMapper.toDto(p, annoNotiCnts.get(p.getId())));
                });
            groupResList.add(groupMapper.toDto(group, pageResList));
        }


        return groupResList;
    }

    public GroupRes createGroup(GroupCreateReq req, Long userId) {
        Project project = projectRepository.findById(req.getProjectId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PROJECT));

        Group group = groupMapper.toEntity(req);
        group.setProject(project);
        GroupRes groupRes = groupMapper.toDto(groupRepository.save(group), new ArrayList<>());

        // 이벤트 발행
        publisher.publishEvent(
                docEvent
                        .resType(WsResType.CREATE_GROUP)
                        .userId(userId)
                        .projectId(project.getProjectId())
                        .data(groupRes).build()
        );
        return groupRes;
    }

    // 순서 변경 (그룹 + 페이지)
    public void updateDocumentsOrder(List<GroupOrderReq> groupOrderReqList, Long userId) {

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

//         이벤트 발행
//        groupPublisher.publishUpdateOrder(userId, projectId);
    }

    public boolean updateGroupName(GroupNameReq req, Long userId) throws NoSuchElementException{
        Group findGroup = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        if (!findGroup.getName().equals(req.getName())) {
            findGroup.updateName(req.getName());
            Group group = groupRepository.save(findGroup);
            // 이벤트 발행

            publisher.publishEvent(
                    docEvent
                            .resType(WsResType.UPDATE_GROUP)
                            .userId(userId)
                            .projectId(findGroup.getProject().getProjectId())
                            .data(groupMapper.toDto(group, new ArrayList<>())).build()
            );
        }
        return true;
    }

    public boolean deleteGroup(Long groupId, Long userId) throws NoSuchElementException{
        Group group =  groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));
        Long projectId = group.getProject().getProjectId();
        groupRepository.delete(group);

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
        // 이벤트 발행
        publisher.publishEvent(
                docEvent
                        .resType(WsResType.DELETE_GROUP)
                        .projectId(projectId)
                        .userId(userId)
                        .data(group.getId()).build()
        );
        return true;
    }
}
