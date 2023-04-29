package com.linking.block.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.block.domain.Block;
import com.linking.block.dto.*;
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

    Logger logger = LoggerFactory.getLogger(BlockService.class);



    @SneakyThrows
    public BlockRes createBlock(BlockCreateReq req, Long userId) {
        Page page = pageRepository.findById(req.getPageId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        if (page.getTemplate() == Template.BLANK) {
            logger.error("cannot add block in Blank template");
            throw new IllegalAccessException("cannot add block in Blank template");
        }
        Block block = blockMapper.toEntity(req);
        block.setPage(page);

        BlockRes blockRes = blockMapper.toDto(blockRepository.save(block));

        // 이벤트 전송
        pageEventHandler.postBlock(page.getId(), userId, new BlockEventRes(blockRes.getBlockId(), blockRes.getPageId(), blockRes.getTitle()));

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
}

