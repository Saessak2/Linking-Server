package com.linking.document.service;

import com.linking.block.service.BlockService;
import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.dto.GroupOrderReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupTempRes;
import com.linking.group.persistence.GroupMapper;
import com.linking.group.persistence.GroupRepository;
import com.linking.group.service.GroupService;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageOrderReq;
import com.linking.page.dto.PageRes;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import com.linking.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final PageMapper pageMapper;
    private final GroupService groupService;
    private final PageService pageService;

    public List<GroupTempRes> findAllDocumentsTemp(Long projectId)  {
        // TODO projectid 가 존재하는지 어떻게 확인하지?

        List<Group> groupList = groupRepository.findAllByProjectId(projectId);
        List<GroupTempRes> groupResList = new ArrayList<>();
        for (Group group : groupList) {
            GroupTempRes groupTempRes = GroupTempRes.builder()
                    .groupId(group.getId())
                    .name(group.getName())
                    .projectId(group.getProject().getProjectId())
                    .build();

            groupResList.add(groupTempRes);
        }
        return groupResList;
    }

    public List<GroupRes> findAllDocuments(Long projectId)  {
        // TODO projectid 가 존재하는지 어떻게 확인하지?

        List<Group> groupList = groupRepository.findAllByProjectId(projectId);
        List<GroupRes> groupResList = new ArrayList<>();
        for (Group group : groupList) {
            GroupRes groupRes = groupMapper.toDto(group);
            List<Page> pageList = group.getPageList();
            if (pageList.size() == 0) {
                //TODO refactoring
                PageRes pageRes = PageRes.builder()
                        .pageId(-1L)
                        .groupId(-1L)
                        .title("")
                        .annotNotiCnt(-1)
                        .build();
                groupRes.getPageResList().add(pageRes);
            }
            else {
                for (Page page : pageList)
                    groupRes.getPageResList().add(pageMapper.toDto(page));
            }
            groupResList.add(groupRes);
        }
        return groupResList;
    }

    public void updateDocumentsOrder(List<GroupOrderReq> reqs) throws RuntimeException{
        try {
            groupService.updateOrder(reqs);
            for (GroupOrderReq groupOrderReq : reqs) {
                pageService.updateOrder(groupOrderReq.getPageList());
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}
