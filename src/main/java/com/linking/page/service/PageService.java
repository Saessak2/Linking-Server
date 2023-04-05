package com.linking.page.service;

import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageRes;
import com.linking.page.dto.PageUpdateReq;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final PageMapper pageMapper;
    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;

    public PageRes createPage(PageCreateReq pageCreateReq) throws Exception{
        // TODO 예외처리
        Project refProject = projectRepository.getReferenceById(pageCreateReq.getProjectId());
        Group refGroup = groupRepository.getReferenceById(pageCreateReq.getParentDocId());

        Page page = pageMapper.toEntity(pageCreateReq);
        page.setParent(refGroup);
        page.setProject(refProject);

        return pageMapper.toDto(pageRepository.save(page));
    }

    public PageRes updatePage(PageUpdateReq pageUpdateReq) throws NoSuchElementException {
        Page findPage = pageRepository.findById(pageUpdateReq.getPageId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        // 그룹 변경
        if (!pageUpdateReq.getParentDocId().equals(findPage.getParent().getId())) {
            Group findGroup = groupRepository.findById(pageUpdateReq.getParentDocId())
                    .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

            findPage.getParent().removePage(findPage);
            findPage.setParent(findGroup);
            findPage.setDocIndex(pageUpdateReq.getDocIndex());
        }
        else {
            findPage.update(pageUpdateReq.getTitle());
        }

        return pageMapper.toDto(pageRepository.save(findPage));
    }

    public void deletePage(Long pageId) throws NoSuchElementException{
        pageRepository.delete(
                pageRepository.findById(pageId).orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE))
        );
    }
}
