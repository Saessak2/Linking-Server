package com.linking.domain.page.dto;

import com.linking.domain.block.dto.BlockDetailRes;
import com.linking.domain.page_check.dto.PageCheckRes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
public class BlockPageDetailRes extends PageDetailedRes{

    private List<BlockDetailRes> blockResList;

    public BlockPageDetailRes(Long pageId, Long groupId, String title, List<PageCheckRes> pageCheckResList, List<BlockDetailRes> blockResList) {
        super(pageId, groupId, title, pageCheckResList);
        this.blockResList = blockResList;
    }
}
