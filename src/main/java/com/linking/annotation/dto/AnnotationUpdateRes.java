package com.linking.annotation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnnotationUpdateRes {

    private Long annotationId;
    private Long blockId;
    private String content;
    private String lastModified;
}
