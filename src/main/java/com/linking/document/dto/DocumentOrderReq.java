package com.linking.document.dto;

import com.linking.group.dto.GroupOrderReq;
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
public class DocumentOrderReq {
    @NotNull
    private List<GroupOrderReq> groupList;
}
