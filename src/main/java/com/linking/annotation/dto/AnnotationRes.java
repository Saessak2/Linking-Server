package com.linking.annotation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnnotationRes {

    private Long annotationId;
    private String content;
    private String lastModified;
    private Long blockId;
    private Long participantId;
    private String userName;
}
