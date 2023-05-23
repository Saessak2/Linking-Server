package com.linking.domain.block.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BlockOrderReq {

    @NotNull
    private Long pageId;

    @NotNull
    private List<Long> blockIds;
}
