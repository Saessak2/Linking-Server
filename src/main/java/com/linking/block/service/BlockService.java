package com.linking.block.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.block.domain.Block;
import com.linking.block.dto.BlockCreateReq;
import com.linking.block.dto.BlockIdRes;
import com.linking.block.dto.BlockOrderReq;
import com.linking.block.dto.BlockRes;
import com.linking.block.persistence.BlockMapper;
import com.linking.block.persistence.BlockRepository;
import com.linking.global.message.ErrorMessage;
import com.linking.page.controller.PageEventHandler;
import com.linking.page.domain.Page;
import com.linking.page.domain.Template;
import com.linking.page.persistence.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final PageEventHandler pageEventHandler;
    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;
    private final PageRepository pageRepository;
    private final AnnotationMapper annotationMapper;

    Logger logger = LoggerFactory.getLogger(BlockService.class);

    public List<BlockRes> toBlockResList(List<Block> blockList) {
        List<BlockRes> blockResList = new ArrayList<>();

        for (Block block : blockList) {
            List<AnnotationRes> annotationResList = new ArrayList<>();
            List<Annotation> annotations = block.getAnnotationList();
            if (annotations.isEmpty()) {
                annotationResList.add(annotationMapper.toEmptyDto());
            } else {
                for (Annotation annotation : block.getAnnotationList()) {
                    annotationResList.add(annotationMapper.toDto(annotation));
                }
            }
            blockResList.add(blockMapper.toDto(block, annotationResList));
        }
        return blockResList;
    }

    @SneakyThrows
    public BlockRes createBlock(BlockCreateReq req, Long userId) {
        Page page = pageRepository.findById(req.getPageId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        if (page.getTemplate() == Template.BLANK) {
            logger.error("블럭을 생성할 수 없는 페이지");
            throw new IllegalAccessException("Blank template에는 블럭을 생성할 수 없습니다.");
        }

        Block block = blockMapper.toEntity(req);
        block.setPage(page);
        List<AnnotationRes> dummy = new ArrayList<>();
        dummy.add(new AnnotationRes(-1L, -1L, "", "00-00-00 AM 00:00", -1L, ""));
        BlockRes blockRes = blockMapper.toDto(blockRepository.save(block), dummy);

        pageEventHandler.postBlock(page.getId(), userId, blockRes);

        return blockRes;
    }

    public void updateBlockOrder(List<BlockOrderReq> req) {
        List<Long> blockIds = req.stream()
                .map(BlockOrderReq::getBlockId)
                .collect(Collectors.toList());
        // 받은 id 순대로 order update 해야함.
        // findAllById 사용시 id 순 대로 정렬돼서 나오는것 같음.
        int count = 0;
        List<Block> blockList = blockRepository.findAllById(blockIds);
        for (Block b : blockList) {
            int order = blockIds.indexOf(b.getId());
            if (b.getBlockOrder() != order) {
                b.updateOrder(order);
                blockRepository.save(b);
                count++;
            }
        }
        logger.info("update block count => {}", count);
    }

    public void deleteBlock(Long blockId, Long userId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_BLOCK));

        Long pageId = block.getPage().getId();
        blockRepository.delete(block);

        List<Block> blockList = blockRepository.findAllByPageId(pageId);
        int order = 0;
        for (Block b : blockList) {
            if (b.getBlockOrder() != order) {
                b.updateOrder(order);
                blockRepository.save(b);
            }
            order++;
        }
        pageEventHandler.deleteBlock(pageId, userId, new BlockIdRes(blockId));
    }

    public Optional<Block> getBlock(Long blockId) {
        return blockRepository.findById(blockId);
    }
}

