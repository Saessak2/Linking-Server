package com.linking.annotation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnnotationCreateReq {

    @NotNull
    @Size(min = 1, max = 255)
    private String content;
    @NotNull
    private Long blockId;
    @NotNull
    private Long participantId;
    @NotNull
    private String userName;
}
