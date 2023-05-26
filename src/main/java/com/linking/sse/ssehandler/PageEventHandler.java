package com.linking.sse.ssehandler;

import com.linking.block.dto.BlockEventRes;
import com.linking.annotation.dto.AnnotationIdRes;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateRes;
import com.linking.block.dto.BlockIdRes;
import com.linking.page_check.dto.PageCheckUpdateRes;
import com.linking.sse.ssehandler.PageSseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PageEventHandler {

    private final PageSseHandler pageSseHandler;

    @Async("eventCallExecutor")
    public void enter(Long pageId, Long publisherId, PageCheckUpdateRes res) {
        log.info("enter async test" + Thread.currentThread());
        pageSseHandler.send(pageId, publisherId, "enter", res);
    }

    @Async("eventCallExecutor")
    public void leave(Long pageId, Long publisherId, PageCheckUpdateRes res) {
        pageSseHandler.send(pageId, publisherId, "leave", res);
    }
    @Async("eventCallExecutor")
    public void postBlock(Long pageId, Long publisherId, BlockEventRes res) {
        pageSseHandler.send(pageId, publisherId, "postBlock", res);
    }
    @Async("eventCallExecutor")
    public void putBlockOrder(Long pageId, Long publisherId, List<Long> blockIds) {
        pageSseHandler.send(pageId, publisherId, "putBlockOrder", blockIds);
    }
    @Async("eventCallExecutor")
    public void deleteBlock(Long pageId, Long publisherId, BlockIdRes res) {
        pageSseHandler.send(pageId, publisherId, "deleteBlock", res);
    }
    @Async("eventCallExecutor")
    public void postAnnotation(Long pageId, Long publisherId, AnnotationRes res) {
        pageSseHandler.send(pageId, publisherId, "postAnnotation", res);
    }
    @Async("eventCallExecutor")
    public void deleteAnnotation(Long pageId, Long publisherId, AnnotationIdRes res) {
        pageSseHandler.send(pageId, publisherId, "deleteAnnotation", res);
    }
    @Async("eventCallExecutor")
    public void updateAnnotation(Long pageId, Long publisherId, AnnotationUpdateRes res) {
        pageSseHandler.send(pageId, publisherId, "updateAnnotation", res);
    }
    @Async("eventCallExecutor")
    public void deletePage(Long pageId, Long publisherId, Long res) {
        pageSseHandler.send(pageId, publisherId, "deletePage", res);
    }
}
