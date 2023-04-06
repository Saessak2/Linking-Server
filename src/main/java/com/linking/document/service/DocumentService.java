package com.linking.document.service;

import com.linking.document.dto.DocumentOrderReq;
import com.linking.document.dto.DocumentRes;
import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.dto.GroupRes;
import com.linking.group.persistence.GroupMapper;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageRes;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public void updateDocumentsOrder(DocumentOrderReq req) {

    }


}
