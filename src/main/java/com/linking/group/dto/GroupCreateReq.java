package com.linking.group.dto;


import com.linking.project.dto.ProjectReqDto;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateReq {

    private Long projectId;
    private String name;
    private int docIndex;
}
