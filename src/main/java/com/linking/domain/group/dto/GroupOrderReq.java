package com.linking.domain.group.dto;

import com.linking.domain.page.dto.PageOrderReq;
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

    @NotNull
    private List<PageOrderReq> pageList;
}
