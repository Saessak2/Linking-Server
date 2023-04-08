package com.linking.block.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.block.domain.Block;
import com.linking.block.dto.BlockCreateReq;
import com.linking.block.dto.BlockOrderReq;
import com.linking.block.dto.BlockRes;
import com.linking.block.persistence.BlockMapper;
import com.linking.block.persistence.BlockRepository;
import com.linking.global.ErrorMessage;
import com.linking.group.dto.GroupRes;
import com.linking.page.domain.Page;
import com.linking.page.persistence.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;
    private final PageRepository pageRepository;
    private final AnnotationMapper annotationMapper;

    public BlockRes createBlock(BlockCreateReq req) {
        Page page = pageRepository.findById(req.getPageId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        Block block = blockMapper.toEntity(req);
        block.setPage(page);

        return blockMapper.toDto(blockRepository.save(block));
    }

    public void updateBlockOrder(List<BlockOrderReq> req) throws RuntimeException{
        List<Long> blockIds = req.stream()
                .map(BlockOrderReq::getBlockId)
                .collect(Collectors.toList());

        int idx = 0;
        for (Long id : blockIds) {
            Block block = blockRepository.findById(id).get();
            block.updateOrder(idx++);
            blockRepository.save(block);
        }


//        try {
//            List<Block> blockList = blockRepository.findAllById(blockIds);
//
//            updateOrder(blockList);
//        } catch (RuntimeException e) { // id 없는경우
//            throw new RuntimeException(e.getMessage());
//        }
    }

    public void deleteBlock(Long blockId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));
        Long pageId = block.getPage().getId();
        blockRepository.delete(block);

        try {
            List<Block> blockList = blockRepository.findAllByPageId(pageId);
            updateOrder(blockList);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<Block> getBlock(Long blockId) {
        return blockRepository.findById(blockId);
    }

    private void updateOrder(List<Block> blockList) {
        int idx = 0;
        for (Block block1 : blockList)
            block1.updateOrder(idx++);
        blockRepository.saveAll(blockList);  // 그새 블럭이 없이지면 어떡하지?
    }

    public List<BlockRes> getBlockResList(Page page) {
        List<Block> blockList = page.getBlockList();

        List<BlockRes> blockResList = new ArrayList<>();
        for (Block block : blockList) {
            List<Annotation> annotations = block.getAnnotationList();
            List<AnnotationRes> annotationResList = new ArrayList<>();
            if (annotations.size() == 0) {
                AnnotationRes annotationRes = AnnotationRes.builder()
                        .annotationId(-1L)
                        .blockId(-1L)
                        .content("")
                        .lastModified("22-01-01 AM 01:01")
                        .userName("")
                        .build();
                annotationResList.add(annotationRes);
            } else {
                for (Annotation annotation : block.getAnnotationList()) {
                    annotationResList.add(annotationMapper.toDto(annotation));
                }
            }
            blockResList.add(blockMapper.toDto(block, annotationResList));
        }
        return blockResList;
    }
}
