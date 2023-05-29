package com.linking.socket.page.service;

import com.linking.page.domain.DiffStr;
import com.linking.page.domain.Page;
import com.linking.page.domain.Template;
import com.linking.page.persistence.PageRepository;
import com.linking.socket.page.PageSocketMessageReq;
import com.linking.socket.page.PageSocketMessageRes;
import com.linking.socket.page.TextSendEvent;
import com.linking.socket.page.persistence.BlankPageSnapshotRepoImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

    private final ApplicationEventPublisher publisher;
    private final BlankPageSnapshotRepoImpl blankPageSnapshotRepoImpl;
    private final PageRepository pageRepository;
    private final ComparisonService comparisonService;

    // todo 페이지 존재하는 지 확인
    public boolean isExistPage(Long pageId) {
        Optional<Page> page = pageRepository.findById(pageId);
        return true;
    }

    public void handleTextMessage(Map<String, Object> attributes, PageSocketMessageReq messageReq) {

        Long pageId = (Long) attributes.get("pageId");
        String sessionId = (String) attributes.get("sessionId");

        String newStr = messageReq.getDocs();
        if (newStr.equals("<br>")){
            messageReq.setDocs("");
        }

        switch (messageReq.getEditorType()) {

            case PAGE_CONTENT:
                String oldStr = blankPageSnapshotRepoImpl.get(pageId);
                // 비교
                DiffStr diffStr = comparisonService.compare(oldStr, newStr);
                if (diffStr != null) {
                    // replace
                    blankPageSnapshotRepoImpl.replace(pageId, newStr);
                    // 전송
                    publisher.publishEvent(constructPageContentEvent(sessionId, pageId, diffStr));
                }

            case BLOCK_TITLE:

            case BLOCK_CONTENT:
        }
    }

    private TextSendEvent constructPageContentEvent(String sessionId, Long pageId, DiffStr diffStr) {

        TextSendEvent event = TextSendEvent.builder()
            .sessionId(sessionId)
            .pageId(pageId)
            .pageSocketMessageRes(
                    PageSocketMessageRes.builder()
                            .editorType(0)
                            .pageId(pageId)
                            .blockId(-1L)
                            .diffStr(diffStr)
                            .build())
        .build();

        return event;
    }

    // 애플리케이션 실행 시 pageContentSnapshotRepoImpl documents 초기화
    @PostConstruct
    public void pageContentSnapshotInit() {

        List<Page> blankPages = pageRepository.findByTemplate(Template.BLANK);
        if (!blankPages.isEmpty()) {
            for (Page page : blankPages) {
                blankPageSnapshotRepoImpl.put(page.getId(), page.getContent());
            }
        }
        log.info("pageContentSnapshot size = {}", blankPageSnapshotRepoImpl.size());
    }

    // page 생성 시 pageContentSnapshotRepoImpl documents 초기화 ("")
    public void pageContentSnapshotInit(Long pageId, String content) {

        blankPageSnapshotRepoImpl.put(pageId, content);
        log.info("페이지 생성 후 pageContentSnapshotInit");
    }

    public String findSnapshotByPageId(Long pageId) {
        return blankPageSnapshotRepoImpl.get(pageId);
    }

    public boolean deletePageSnapshot(Long pageId, Template template) {

        if (template == Template.BLANK) {
            return blankPageSnapshotRepoImpl.delete(pageId);

        } else if (template == Template.BLOCK){
            return true;
        }
        return false;
    }
}
