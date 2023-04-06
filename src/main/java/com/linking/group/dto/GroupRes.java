package com.linking.group.dto;

import com.linking.page.dto.PageRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRes{

    private Long groupId;
    private Long projectId;
    private String name;
    private List<PageRes> pageResList;
}
