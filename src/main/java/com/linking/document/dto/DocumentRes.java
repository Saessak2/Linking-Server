package com.linking.document.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRes {

    private Long docId;
    private String title;
    private int docIndex;
    private String DType;
}
