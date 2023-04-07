package com.linking.block.service;

import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.block.domain.Block;
import com.linking.block.dto.BlockCreateReq;
import com.linking.block.dto.BlockRes;
import com.linking.block.persistence.BlockMapper;
import com.linking.block.persistence.BlockRepository;
import com.linking.global.ErrorMessage;
import com.linking.group.dto.GroupRes;
import com.linking.page.domain.Page;
import com.linking.page.persistence.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;
    private final PageRepository pageRepository;

    public BlockRes createBlock(BlockCreateReq req) {
        Page page = pageRepository.findById(req.getPageId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        Block block = blockMapper.toEntity(req);
        block.setPage(page);

        return blockMapper.toDto(blockRepository.save(block));
    }


//    private final BlockRepository blockRepository;
//    private final BlockMapper blockMapper;
//    private final AnnotationMapper annotationMapper;
//    private final PageRepository pageRepository;
//

}
