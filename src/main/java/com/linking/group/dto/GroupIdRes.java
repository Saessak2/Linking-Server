package com.linking.group.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GroupIdRes {

    private Long groupId;

    public GroupIdRes(Long groupId) {
        this.groupId = groupId;
    }
}
