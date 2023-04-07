package com.linking.document.service;

import com.linking.document.dto.DocumentOrderReq;
import com.linking.document.dto.DocumentRes;
import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.dto.GroupOrderReq;
import com.linking.group.dto.GroupRes;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final GroupRepository groupRepository;
    private final PageRepository pageRepository;
    private final GroupMapper groupMapper;
    private final PageMapper pageMapper;


    public List<GroupRes> findAllDocuments(Long projectId) {
        // TODO projectid 가 존재하는지 어떻게 확인하지?

        List<Group> groupList = groupRepository.findAllByProject(projectId);
        List<GroupRes> groupResList = new ArrayList<>();
        for (Group group : groupList) {
            GroupRes groupRes = groupMapper.toDto(group);
            for (Page page : group.getPageList())
                groupRes.getPageResList().add(pageMapper.toDto(page));
            groupResList.add(groupRes);
        }
        return groupResList;
    }

    public void updateDocumentsOrder(List<GroupOrderReq> req) throws NoSuchElementException{
        // TODO 예외처리
        // TODO 성능 최적화 - 쿼리 많이 안나가게
        int groupOrder = 0;
        for (GroupOrderReq groupOrderReq : req) {
            int pageOrder = 0;
            for (PageOrderReq pageOrderReq: groupOrderReq.getPageList()) {
                Page page = pageRepository.findById(pageOrderReq.getPageId())
                        .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));
                page.updateOrder(pageOrder++);
                pageRepository.save(page);
            }
            Group group = groupRepository.findById(groupOrderReq.getGroupId())
                    .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));
            group.updateOrder(groupOrder++);
            groupRepository.save(group);
        }
    }
}
