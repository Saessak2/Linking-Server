package com.linking.annotation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/annotations")
@RequiredArgsConstructor
public class AnnotationController {

    private final AnnotationService annotationService;
}
