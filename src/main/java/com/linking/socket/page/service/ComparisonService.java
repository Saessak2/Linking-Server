package com.linking.socket.page.service;

import com.linking.page.domain.DiffStr;
import com.linking.global.util.StringComparison;
import com.linking.socket.page.TextInputMessage;
import com.linking.socket.page.TextOutputMessage;
import com.linking.socket.page.TextSendEvent;
import com.linking.socket.page.persistence.PageContentSnapshotRepoImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComparisonService {
    private final PageContentSnapshotRepoImpl pageContentSnapshotRepoImpl;
    private final ApplicationEventPublisher publisher;

    @Async("eventCallExecutor")
    public void compare(String sessionId, Long pageId, TextInputMessage message) {

        String oldStr = pageContentSnapshotRepoImpl.poll(pageId);
        String newStr = message.getDocs();
        DiffStr diffStr = StringComparison.compareString(oldStr, newStr);

        if (diffStr == null) {
            log.info("Not Modified");
            return;
        }
        log.info("=================================================================");
        log.info("snapshot size = {}", pageContentSnapshotRepoImpl.sizeByPage(pageId));
        log.info("oldStr = {}", oldStr);
        log.info("newStr = {}", newStr);
        log.info("type : {}, start : {}, end : {}, subStr : {}", diffStr.getType(), diffStr.getDiffStartIndex(), diffStr.getDiffEndIndex(), diffStr.getSubStr());

        TextSendEvent event = TextSendEvent.builder()
                .sessionId(sessionId)
                .pageId(pageId)
                .textOutputMessage(
                        TextOutputMessage.builder()
                                .editorType(0)
                                .pageId(pageId)
                                .blockId(-1L)
                                .diffStr(diffStr)
                                .build()
                )
                .build();

        publisher.publishEvent(event);

    }
}
