package com.linking.block.dto;

import com.linking.annotation.dto.AnnotationRes;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BlockRes {

    private Long blockId;
    private Long pageId;
    private String title;
    private List<AnnotationRes> annotationResList;
}
