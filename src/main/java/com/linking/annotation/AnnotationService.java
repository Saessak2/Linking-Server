package com.linking.annotation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnotationService {
    private final AnnotationRepository annotationRepository;
}
