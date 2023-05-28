package com.linking.socket.page.service;

import com.linking.page.domain.Page;
import com.linking.page.domain.Template;
import com.linking.page.persistence.PageRepository;
import com.linking.socket.page.PageSocketMessageReq;
import com.linking.socket.page.persistence.PageContentSnapshotRepoImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageWebSocketService {

    private static final int PAGE_CONTENT = 0;
    private static final int BLOCK_TITLE = 1;
    private static final int BLOCK_CONTENT = 2;

    private final PageContentSnapshotRepoImpl pageContentSnapshotRepoImpl;
    private final PageRepository pageRepository;
    private final ComparisonService comparisonService;

    // todo 페이지 존재하는 지 확인
    public boolean isExistPage(Long pageId) {
        Optional<Page> page = pageRepository.findById(pageId);
        return true;
    }

    public void inputText(Map<String, Object> attributes, PageSocketMessageReq pageSocketMessageReq) {

        int editorType = pageSocketMessageReq.getEditorType();
        Long pageId = (Long) attributes.get("pageId");
        String sessionId = (String) attributes.get("sessionId");

        if (editorType == PAGE_CONTENT) {

            pageContentSnapshotRepoImpl.add(pageId, pageSocketMessageReq.getDocs());
            comparisonService.compare(sessionId, pageId, pageSocketMessageReq);

        } else if (editorType == BLOCK_TITLE) {

        } else if (editorType == BLOCK_CONTENT) {

        } else {
            return;
        }
    }

    // 페이지 content snapshot 초기화
    @PostConstruct
    public void pageContentSnapshotInit() {

        List<Page> blankPages = pageRepository.findByTemplate(Template.BLANK);
        if (!blankPages.isEmpty()) {
            for (Page page : blankPages) {
                pageContentSnapshotRepoImpl.put(page.getId(), page.getContent());
            }
        }
        log.info("pageContentSnapshot size = {}", pageContentSnapshotRepoImpl.mapSize());
    }

    public void pageContentSnapshotInit(Long pageId, String content) {
        pageContentSnapshotRepoImpl.put(pageId, content);
    }


    public String findSnapshotByPageId(Long pageId) {
        return pageContentSnapshotRepoImpl.peek(pageId);
    }


}
