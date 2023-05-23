package com.linking.domain.group.dto;

import com.linking.domain.page.dto.PageRes;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRes {

    private Long projectId;
    private Long groupId;
    private String name;
    private List<PageRes> pageResList;
}
