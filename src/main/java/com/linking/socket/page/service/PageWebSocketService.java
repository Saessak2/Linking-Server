package com.linking.socket.page.service;

import com.linking.block.domain.Block;
import com.linking.page.domain.DiffStr;
import com.linking.page.domain.Page;
import com.linking.page.domain.Template;
import com.linking.page.persistence.PageRepository;
import com.linking.socket.page.BlockSnapshot;
import com.linking.socket.page.PageSocketMessageReq;
import com.linking.socket.page.PageSocketMessageRes;
import com.linking.socket.page.TextSendEvent;
import com.linking.socket.page.persistence.BlankPageSnapshotRepo;
import com.linking.socket.page.persistence.BlockPageSnapshotRepo;
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

    private final BlankPageSnapshotRepo blankPageSnapshotRepo;
    private final BlockPageSnapshotRepo blockPageSnapshotRepo;

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
                String oldStr = blankPageSnapshotRepo.get(pageId);
                // 비교
                DiffStr diffStr = comparisonService.compare(oldStr, newStr);
                if (diffStr != null) {
                    // replace
                    blankPageSnapshotRepo.replace(pageId, newStr);
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
    public void createBlankPage() {

        List<Page> blankPages = pageRepository.findByTemplate(Template.BLANK);
        if (!blankPages.isEmpty()) {
            for (Page page : blankPages) {
                blankPageSnapshotRepo.put(page.getId(), page.getContent());
            }
        }
    }

    @PostConstruct
    public void initPages() {

        List<Page> allPages = pageRepository.findAll();
        for (Page page : allPages) {
            if (page.getTemplate() == Template.BLANK) {
                blankPageSnapshotRepo.put(page.getId(), page.getContent());
            }
            else if (page.getTemplate() == Template.BLOCK) {
                blockPageSnapshotRepo.putPage(page.getId());
                for (Block block : page.getBlockList()) {
                    blockPageSnapshotRepo.putBlock(page.getId(), block.getId(), new BlockSnapshot(block.getTitle(), block.getContent()));
                }
            }
        }
        log.info("빈 페이지 Snapshot size = {}", blankPageSnapshotRepo.size());
        log.info("블럭 페이지 Snapshot size = {}", blankPageSnapshotRepo.size());

    }

    public boolean deletePageSnapshot(Long pageId, Template template) {

        if (template == Template.BLANK) {
            return blankPageSnapshotRepo.delete(pageId);

        } else if (template == Template.BLOCK){
            return blockPageSnapshotRepo.delete(pageId);
        }
        return false;
    }

    // blank page

    public void createBlankPage(Long pageId, String content) {

        blankPageSnapshotRepo.put(pageId, content);
        log.info("페이지 생성 후 pageContentSnapshotInit");
    }

    public String findBlankPageSnapshot(Long pageId) {
        return blankPageSnapshotRepo.get(pageId);
    }

    // block page

    public void createBlockPage(Long pageId) {
        blockPageSnapshotRepo.putPage(pageId);
    }

    public void createBlock(Long pageId, Long blockId, BlockSnapshot blockSnapshot) {
        blockPageSnapshotRepo.putBlock(pageId, blockId, blockSnapshot);
    }

    public Map<Long, BlockSnapshot> findBlockPageSnapshot(Long pageId) {
        return blockPageSnapshotRepo.findByPageId(pageId);
    }
}
