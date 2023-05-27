package com.linking.page.service;

import com.linking.page.domain.Page;
import com.linking.page.domain.Template;
import com.linking.page.dto.TextInputMessage;
import com.linking.page.persistence.PageContentSnapshotRepoImpl;
import com.linking.page.persistence.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageWebSocketService {

    private static final int PAGE_CONTENT = 0;
    private static final int BLOCK_TITLE = 1;
    private static final int BLOCK_CONTENT = 2;

    private final PageContentSnapshotRepoImpl pageContentSnapshotRepoImpl;
    private final ApplicationEventPublisher publisher;
    private final PageRepository pageRepository;
    private final ComparisonService comparisonService;

    public void inputText(Map<String, Object> attributes, TextInputMessage textInputMessage) {

        log.info("inputText -------- {}", Thread.currentThread().getName());

        int editorType = textInputMessage.getEditorType();
        Long pageId = (Long) attributes.get("pageId");
        String sessionId = (String) attributes.get("sessionId");

        if (editorType == PAGE_CONTENT) {

//            TextInput textInput = toTextInput(attributes, textInputMessage);
//            Queue<TextInput> queue = pageContentInputRepoImpl.save((Long) attributes.get("pageId"), textInput);

            pageContentSnapshotRepoImpl.add(pageId, textInputMessage.getDocs());
            comparisonService.compare(sessionId, pageId, textInputMessage);

        } else if (editorType == BLOCK_TITLE) {

        } else if (editorType == BLOCK_CONTENT) {

        } else {
            return;
        }
    }

    // todo 페이지 생성할떄도 저장 해야함.

    // 페이지 content snapshot 초기화
    @PostConstruct
    public void PageContentSnapshotInit() {

        List<Page> blankPages = pageRepository.findByTemplate(Template.BLANK);
        if (!blankPages.isEmpty()) {
            for (Page page : blankPages) {
                pageContentSnapshotRepoImpl.put(page.getId(), page.getContent());
            }
        }
        log.info("pageContentSnapshot size = {}", pageContentSnapshotRepoImpl.mapSize());
    }

    public String findSnapshotByPageId(Long pageId) {
        return pageContentSnapshotRepoImpl.peek(pageId);
    }
}
