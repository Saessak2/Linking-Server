package com.linking.ws.service;


import com.linking.group.service.GroupService;
import com.linking.ws.message.WsMessage;
import com.linking.ws.handler.WsResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WsDocumentService {

    private final GroupService groupService;

    public WsMessage getAllDocumentsByProjectId(Long projectId) {

        return new WsMessage(
                WsResponseType.GROUPS, WsResponseType.READ,
                groupService.findAllGroups(projectId));
    }


}
