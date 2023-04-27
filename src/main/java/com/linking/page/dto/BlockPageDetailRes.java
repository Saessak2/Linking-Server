package com.linking.page.dto;

import com.linking.block.dto.BlockRes;
import com.linking.pageCheck.dto.PageCheckRes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
public class BlockPageDetailRes extends PageDetailedRes{

    private List<BlockRes> blockResList;

    public BlockPageDetailRes(Long pageId, Long groupId, String title, List<PageCheckRes> pageCheckResList, List<BlockRes> blockResList) {
        super(pageId, groupId, title, pageCheckResList);
        this.blockResList = blockResList;
    }
}
