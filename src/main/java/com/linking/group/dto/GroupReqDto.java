package com.linking.group.dto;


import com.linking.project.dto.ProjectReqDto;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupReqDto {

    private String name;
    private int docDepth;
    private int docIndex;
    private ProjectReqDto projectDto;

}
