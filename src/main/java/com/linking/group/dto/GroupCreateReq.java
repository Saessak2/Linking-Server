package com.linking.group.dto;


import com.linking.project.dto.ProjectReqDto;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateReq {

    @NotNull
    private Long projectId;
    @NotNull
    @Size(min = 1, max = 10)
    private String name;
    @NotNull
    private int docIndex;
}
