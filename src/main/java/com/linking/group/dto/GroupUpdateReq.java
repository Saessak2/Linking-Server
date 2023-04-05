package com.linking.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupUpdateReq {

    @NotNull
    private Long groupId;
    @NotNull
    private int order;
    @NotNull
    @Size(min = 1, max = 10)
    private String title;
}
