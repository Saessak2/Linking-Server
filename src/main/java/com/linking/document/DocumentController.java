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
}
