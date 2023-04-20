package com.linking.group.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.linking.global.util.JsonMapper;
import com.linking.global.common.WsResType;
import com.linking.ws.message.WsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class WsDocumentService {

    private final GroupService groupService;

    public TextMessage getAllDocuments(Long projectId, Long userId) throws IOException {
        try {
            return new TextMessage(JsonMapper.toJsonString(new WsMessage<>(
                    WsResType.ALL_GROUPS,
                    groupService.findAllGroups(projectId, userId))
            ));
        } catch (JsonProcessingException e) {
            throw new IOException("JsonMapper.toJsonString IOException");
        }
    }
}

