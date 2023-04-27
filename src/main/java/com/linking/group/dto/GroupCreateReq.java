package com.linking.group.dto;


import lombok.*;
import javax.validation.constraints.NotNull;

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
