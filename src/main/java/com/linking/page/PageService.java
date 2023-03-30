package com.linking.page;

import com.linking.page.dto.PageReqDto;
import com.linking.project.Project;
import com.linking.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final ProjectRepository projectRepository;


    public void createPage(PageReqDto pageReqDto) throws NoSuchElementException{

        Project project = projectRepository.findById(pageReqDto.getProjectId())
                .orElseThrow(() -> new NoSuchElementException());

        Page page = Page.builder()
                .title(pageReqDto.getName())
                .doc_depth(pageReqDto.getDocDepth())
                .doc_index(pageReqDto.getDocIndex())
                .project(project)
                .createdDatetime(LocalDateTime.now())
                .updatedDatetime(LocalDateTime.now())
                .pageCheckList(new ArrayList<>())
                .blockList(new ArrayList<>())
                .build();

        pageRepository.save(page);
    }
}
