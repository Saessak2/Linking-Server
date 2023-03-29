package com.linking.document;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/:id")
    public List<Document> requestAllDocuments(@RequestParam Long projectId) {
        List<Document> allDocuments = documentService.findAllDocuments(projectId);
        return allDocuments;
    }

    // TODO createNewDocument는 프로젝트 생성 시 -> documentService에서 처리

}
