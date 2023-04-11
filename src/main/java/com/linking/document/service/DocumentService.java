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

    // 문서리스트 조회 (그룹 + 페이지)
    public List<GroupRes> findAllDocuments(Long projectId)  {

        List<Group> groupList = groupRepository.findAllByProjectId(projectId);
        List<GroupRes> groupResList = new ArrayList<>();
        for (Group group : groupList) {
            GroupRes groupRes = groupMapper.toDto(group);
            List<Page> pageList = group.getPageList();
            if (!pageList.isEmpty()) {
                for (Page page : pageList)
                    groupRes.getPageResList().add(pageMapper.toDto(page));
            }
            groupResList.add(groupRes);
        }
        return groupResList;
    }

    // 문서 순서 변경 (그룹 + 페이지)
    public void updateDocumentsOrder(List<GroupOrderReq> groupOrderReqList) throws RuntimeException{
        groupService.updateOrder(groupOrderReqList);
        for (GroupOrderReq groupOrderReq : groupOrderReqList)
            pageService.updateOrder(groupOrderReq.getPageList());
    }
}
