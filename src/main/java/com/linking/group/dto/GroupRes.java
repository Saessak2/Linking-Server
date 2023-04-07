package com.linking.group.dto;

import com.linking.page.dto.PageDetailedRes;
import com.linking.page.dto.PageRes;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRes{

    private Long groupId;
    private Long projectId;
    private String name;
    private List<PageRes> pageResList;
}
