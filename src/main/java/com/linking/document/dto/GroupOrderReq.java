package com.linking.document.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupOrderReq {
    @NotNull
    private Long groupId;
    private List<PageOrderReq> pageOrderReqList;
}
