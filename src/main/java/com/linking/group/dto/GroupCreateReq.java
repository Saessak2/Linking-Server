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

    private String title;

    @NotNull
    private int order;
}
