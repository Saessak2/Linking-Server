package com.linking.group.service;

import com.linking.global.message.ErrorMessage;
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
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ProjectRepository projectRepository;
    private final PageRepository pageRepository;
    private final PageMapper pageMapper;
    private final PageCheckRepository pageCheckRepository;

    // 그룹 리스트 조회
    public List<GroupDetailedRes> findAllGroups(Long projectId, Long userId)  {

        List<Group> groupList = groupRepository.findAllByProjectId(projectId);
        if (groupList.isEmpty())
            return new ArrayList<>();

        // annoNotCnt를 위해 pageCheck 조회
//        Participant participant = participantRepository.findByUserAndProjectId(userId, projectId)
//                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PARTICIPANT));
        List<PageCheck> pageCheckList = pageCheckRepository.findAllByParticipant(userId, projectId);
        // TODO user, project CHECK

        Map<Long, Integer> annoNotCnts = new HashMap<>(); // key -> pageId
        pageCheckList.forEach(pc -> {
            annoNotCnts.put(pc.getPage().getId(), pc.getAnnoNotCount());
        });

        List<GroupDetailedRes> groupDetailedResList = new ArrayList<>();
        for (Group group : groupList) {  // order 순서
            List<PageRes> pageResList = new ArrayList<>();
            List<Page> pageList = group.getPageList();   // order 순서
            if (!pageList.isEmpty())
                pageList.forEach(p -> {
                    pageResList.add(pageMapper.toDto(p, annoNotCnts.get(p.getId())));
                });
            groupDetailedResList.add(groupMapper.toDto(group, pageResList));
        }

        return groupDetailedResList;
    }

    public GroupRes createGroup(GroupCreateReq req) {
        Project project = projectRepository.findById(req.getProjectId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PROJECT));

        Group group = groupMapper.toEntity(req);
        group.setProject(project);
        return groupMapper.toDto(groupRepository.save(group));
    }

    public GroupRes updateGroupName(GroupNameReq req) {

        Group findGroup = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        if (!findGroup.getName().equals(req.getName())) {
            findGroup.updateName(req.getName());
            GroupRes groupRes = groupMapper.toDto(groupRepository.save(findGroup));
            return groupRes;
        }
        return null;
    }

    // 순서 변경 (그룹 + 페이지)
    public boolean updateDocumentsOrder(List<GroupOrderReq> groupOrderReqList, Long userId) {

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



    public Map<String, Object> deleteGroup(Long groupId) throws NoSuchElementException{

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

        Map<String, Object> returnVal = new HashMap<>();
        returnVal.put("projectId", group.getProject().getProjectId());
        returnVal.put("data", new GroupIdRes(groupId));
        return returnVal;
    }
}
