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

    public void createPage(PageReqDto pageReqDto) throws NoSuchElementException{


    }
}
