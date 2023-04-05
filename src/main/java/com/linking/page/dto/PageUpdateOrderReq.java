package com.linking.page.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageUpdateOrderReq {

    private Long pageId;
    private Long beforeGroupId;
    private Long afterGroupId;
    private int changedOrder;
}
