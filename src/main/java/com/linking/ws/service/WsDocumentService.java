package com.linking.ws.service;


import com.linking.document.service.DocumentService;
import com.linking.ws.dto.WsMessage;
import com.linking.ws.handler.WsResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WsDocumentService {

    private final DocumentService documentService;

    public WsMessage getAllDocumentsByProjectId(Long projectId) {

        return new WsMessage(WsResponseType.ALL_GROUPS, documentService.findAllDocuments(projectId));
    }
}
