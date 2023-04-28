package com.linking.page.controller;

import com.linking.pageCheck.dto.PageCheckUpdateRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageEventHandler {

    private final PageSseHandler pageSseHandler;

    public void leave(Long pageId, Long publisherId, PageCheckUpdateRes res) {
        pageSseHandler.send(pageId, publisherId, "leave", res);
    }
}
