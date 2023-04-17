package com.linking.ws.service;

import com.linking.page.dto.PageDetailedRes;
import com.linking.page.service.PageService;
import com.linking.pageCheck.dto.PageCheckRes;
import com.linking.pageCheck.service.PageCheckService;
import com.linking.ws.code.WsResType;
import com.linking.ws.message.WsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WsPageService {

    private final PageService pageService;
    private final PageCheckService pageCheckService;

    public WsMessage getPage(Long pageId, Long projectId, Long userId, List<Long> enteringUsers) {
        PageDetailedRes page = pageService.getPage(pageId, userId);
        page.getPageCheckResList().forEach(pc -> {
            if (enteringUsers.contains(pc.getUserId()))
                pc.setIsEntering(true);
            else
                pc.setIsEntering(false);
        });
        return new WsMessage(WsResType.PAGE, page);
    }

    public WsMessage updatePageLastChecked(Long pageId, Long projectId, Long userId) {
        PageCheckRes pageCheckRes = pageCheckService.updatePageLastChecked(pageId, projectId, userId);
        if (pageCheckRes != null)
            return new WsMessage(WsResType.PAGE_CHECK, pageCheckRes);
        return null;
    }
}
