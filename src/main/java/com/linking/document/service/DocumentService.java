package com.linking.document.service;

import com.linking.document.dto.DocumentOrderReq;
import com.linking.document.dto.DocumentRes;
import com.linking.global.ErrorMessage;
import com.linking.group.persistence.GroupMapper;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;
    private final PageRepository pageRepository;
    private final GroupMapper groupMapper;
    private final PageMapper pageMapper;


    public List<DocumentRes> findAllDocuments(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PROJECT));


    }

    public void updateDocumentsOrder(DocumentOrderReq req) {

    }


}
