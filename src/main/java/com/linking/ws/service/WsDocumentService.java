package com.linking.ws.service;


import com.linking.group.service.GroupService;
import com.linking.global.common.WsResType;
import com.linking.ws.message.WsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WsDocumentService {

    private final GroupService groupService;

    public WsMessage getAllDocuments(Long projectId, Long userId) {

        return new WsMessage(
                WsResType.ALL_GROUPS,
                groupService.findAllGroups(projectId, userId));
    }
}
