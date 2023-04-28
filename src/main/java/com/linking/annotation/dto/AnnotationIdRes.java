package com.linking.annotation.dto;

public class AnnotationIdRes {

    private Long annotationId;
    private Long blockId;

    public AnnotationIdRes(Long annotationId, Long blockId) {
        this.annotationId = annotationId;
        this.blockId = blockId;
    }
}
