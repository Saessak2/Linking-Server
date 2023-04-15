package com.linking.group.dto;


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
    private String name;
    @NotNull
    private int order;
}
