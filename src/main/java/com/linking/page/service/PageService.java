package com.linking.page.service;

import com.linking.document.domain.Document;
import com.linking.document.persistence.DocumentMapper;
import com.linking.document.persistence.DocumentRepository;
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
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    public PageRes createPage(PageCreateReq pageCreateReq) throws Exception{
        // TODO 예외처리
        Project refProject = projectRepository.getReferenceById(pageCreateReq.getProjectId());
        Document refDoc = documentRepository.getReferenceById(pageCreateReq.getParentDocId());

        Document document = documentMapper.toEntity(pageCreateReq);
        document.setParent(refDoc);
        document.setProject(refProject);
//        documentRepository.save(document);

        Page page = pageMapper.toEntity(pageCreateReq);
        page.setDocument(document);
        pageRepository.save(page);

        PageRes pageRes = PageRes.builder()
                .docId(document.getId())
                .title(document.getTitle())
                .createdDatetime(page.getCreatedDatetime().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")))
                .updatedDatetime(page.getUpdatedDatetime().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")))
                .projectId(document.getProject().getProjectId())
                .parentDocId(document.getParent().getId())
                .build();

        return pageRes;
    }

//    public PageRes updatePage(PageUpdateReq pageUpdateReq) throws NoSuchElementException {
//        Page findPage = pageRepository.findById(pageUpdateReq.getPageId())
//                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));
//
//        // 그룹 변경
//        if (!pageUpdateReq.getParentDocId().equals(findPage.getParent().getId())) {
//            Group findGroup = groupRepository.findById(pageUpdateReq.getParentDocId())
//                    .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));
//
//            findPage.getParent().removePage(findPage);
//            findPage.setParent(findGroup);
//            findPage.setDocIndex(pageUpdateReq.getDocIndex());
//        }
//        else {
//            findPage.update(pageUpdateReq.getTitle());
//        }
//
//        return pageMapper.toDto(pageRepository.save(findPage));
//    }

    public void deletePage(Long docId) throws NoSuchElementException{
        Document findDocument = documentRepository.findById(docId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));
        // 그룹 삭제 시 페이지 모두 삭제하는 경우
//        findGroup.removeAllPages();
        documentRepository.delete(findDocument);
        // TODO 그룹도 삭제되는지 확인

        // TODO 프로젝트의 document 리스트에서 삭제 되는지 확인 필요함

    }

    public PageRes getPage(Long docId) throws NoSuchElementException{
        Page findPage = pageRepository.findById(docId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        PageRes.PageResBuilder builder = PageRes.builder();
        builder
                .title(findPage.getDocument().getTitle())

    }
}
