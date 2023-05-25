package com.linking.group.dto;

import com.linking.page.dto.PageRes;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GroupDetailedRes {

    private Long groupId;
    private Long projectId;
    private String name;
    private List<PageRes> pageResList;
}
