package com.linking.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRes{

    private Long groupId;
    private Long projectId;
    private int order;
    private String title;
}
