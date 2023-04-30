package com.linking.page.controller;

import com.linking.annotation.dto.AnnotationIdRes;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateRes;
import com.linking.block.dto.BlockEventRes;
import com.linking.block.dto.BlockIdRes;
import com.linking.pageCheck.dto.PageCheckUpdateRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PageEventHandler {

    private final PageSseHandler pageSseHandler;

    public void enter(Long pageId, Long publisherId, PageCheckUpdateRes res) {
        pageSseHandler.send(pageId, publisherId, "enter", res);
    }
    public void leave(Long pageId, Long publisherId, PageCheckUpdateRes res) {
        pageSseHandler.send(pageId, publisherId, "leave", res);
    }

    public void postBlock(Long pageId, Long publisherId, BlockEventRes res) {
        pageSseHandler.send(pageId, publisherId, "postBlock", res);
    }

    public void putBlockOrder(Long pageId, Long publisherId, List<Long> blockIds) {
        pageSseHandler.send(pageId, publisherId, "putBlockOrder", blockIds);
    }

    public void deleteBlock(Long pageId, Long publisherId, BlockIdRes res) {
        pageSseHandler.send(pageId, publisherId, "deleteBlock", res);
    }

    public void postAnnotation(Long pageId, Long publisherId, AnnotationRes res) {
        pageSseHandler.send(pageId, publisherId, "postAnnotation", res);
    }

    public void deleteAnnotation(Long pageId, Long publisherId, AnnotationIdRes res) {
        pageSseHandler.send(pageId, publisherId, "deleteAnnotation", res);
    }

    public void updateAnnotation(Long pageId, Long publisherId, AnnotationUpdateRes res) {
        pageSseHandler.send(pageId, publisherId, "updateAnnotation", res);
    }
}
