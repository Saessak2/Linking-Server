package com.linking.group.dto;

import com.linking.document.dto.DocumentRes;
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

    private Long docId;
    private Long projectId;
    private String title;
}
