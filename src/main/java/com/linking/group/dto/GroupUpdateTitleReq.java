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
public class GroupUpdateTitleReq {

    @NotNull
    private Long groupId;
    @NotNull
    @Size(max = 10)
    private String name;
}
