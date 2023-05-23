package com.linking.domain.group.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupPostEvent {

    private Long groupId;
    private String name;

    @Builder
    public GroupPostEvent(Long groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }
}
