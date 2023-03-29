package com.linking.document.dto;

import lombok.Builder;

@Builder
public class ParentGroupDto {
    private Long id;

    public ParentGroupDto() {
    }

    public Long getId() {
        return id;
    }
}
