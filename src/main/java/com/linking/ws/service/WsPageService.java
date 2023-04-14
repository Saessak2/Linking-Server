package com.linking.ws.service;

import com.linking.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WsPageService {

    private PageService pageService;
}
