package com.linking.document.dto;

import com.linking.group.dto.GroupRes;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DocumentRes {
    private List<GroupRes> groupResList;
}
