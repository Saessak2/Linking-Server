package com.linking.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    public List<Document> findAllDocuments(Long projectId) {
        return documentRepository.findAllByProjectId(projectId);
    }

}
