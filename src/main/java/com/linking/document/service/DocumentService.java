package com.linking.document.service;

import com.linking.document.persistence.DocumentMapper;
import com.linking.document.persistence.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;



}
