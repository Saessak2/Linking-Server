package com.linking.page.service;

import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageRes;
import com.linking.page.dto.PageUpdateReq;
import com.linking.page.persistence.PageRepository;
import com.linking.project.ProjectRepository;
import com.linking.project.domain.Project;
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
    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;

    public PageRes createPage(PageCreateReq pageCreateReq) throws NoSuchElementException{
        Project findProject = projectRepository.findById(pageCreateReq.getProjectId())
                .orElseThrow(() -> new NoSuchElementException());

        Group findGroup = groupRepository.findById(pageCreateReq.getParentDocId())
                .orElseThrow(() -> new NoSuchElementException());

        Page page = Page.builder()
                .project(findProject)
                .title(pageCreateReq.getTitle())
                .docIndex(pageCreateReq.getDocIndex())
                .createdDatetime(LocalDateTime.now())
                .updatedDatetime(LocalDateTime.now())
                .blockList(new ArrayList<>())
                .pageCheckList(new ArrayList<>())
                .build();
        page.setParent(findGroup);

        Page savePage = pageRepository.save(page);

        PageRes pageRes = PageRes.builder()
                .pageId(savePage.getId())
                .projectId(savePage.getProject().getProjectId())
                .parentDocId(savePage.getParent().getId())
                .title(savePage.getTitle())
                .createdDatetime(page.getCreatedDatetime().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")))
                .updatedDatetime(page.getUpdatedDatetime().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")))
                .build();
        return pageRes;
    }

    public PageRes updatePage(PageUpdateReq pageUpdateReq) throws NoSuchElementException {
        Page findPage = pageRepository.findById(pageUpdateReq.getPageId())
                .orElseThrow(() -> new NoSuchElementException());

        // 그룹 변경
        if (!pageUpdateReq.getParentDocId().equals(findPage.getParent().getId())) {
            Group findGroup = groupRepository.findById(pageUpdateReq.getParentDocId())
                    .orElseThrow(() -> new NoSuchElementException());

            findPage.getParent().getChildList().remove(this);
            findPage.setParent(findGroup);
            findPage.setDocIndex(pageUpdateReq.getDocIndex());
        }
        else {
            findPage.update(pageUpdateReq.getTitle());
        }
        pageRepository.save(findPage);

        PageRes pageRes = PageRes.builder()
                .pageId(findPage.getId())
                .title(pageUpdateReq.getTitle())
                .projectId(findPage.getProject().getProjectId())
                .parentDocId(findPage.getParent().getId())
                .createdDatetime(findPage.getCreatedDatetime().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")))
                .updatedDatetime(findPage.getUpdatedDatetime().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")))
                .build();

        return pageRes;
    }

    public void deletePage(Long pageId) throws NoSuchElementException{
        pageRepository.delete(
                pageRepository.findById(pageId).orElseThrow(() -> new NoSuchElementException())
        );
    }
}
