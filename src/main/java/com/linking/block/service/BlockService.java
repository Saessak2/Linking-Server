package com.linking.block.service;

import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.block.domain.Block;
import com.linking.block.dto.BlockCreateReq;
import com.linking.block.dto.BlockRes;
import com.linking.block.persistence.BlockMapper;
import com.linking.block.persistence.BlockRepository;
import com.linking.page.domain.Page;
import com.linking.page.persistence.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;
    private final AnnotationMapper annotationMapper;
    private final PageRepository pageRepository;

//    public void createBlock(BlockCreateReq blockCreateReq) {
//        Page refPage = pageRepository.getReferenceById(blockCreateReq.getPageId());
//        Block block = blockMapper.toEntity(blockCreateReq);
//        block.setPage(refPage);
//
//    }
}
