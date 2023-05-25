package com.linking.block.service;

import com.linking.block.persistence.BlockMapper;
import com.linking.block.domain.Block;
import com.linking.block.dto.*;
import com.linking.block.persistence.BlockRepository;
import com.linking.page.controller.PageEventHandler;
import com.linking.page.domain.Page;
import com.linking.page.domain.Template;
import com.linking.page.persistence.PageRepository;
import com.linking.global.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BlockService {
    private final PageEventHandler pageEventHandler;
    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;
    private final PageRepository pageRepository;


    @SneakyThrows
    @Transactional
    public BlockRes createBlock(BlockCreateReq req, Long userId) {

        Page page = pageRepository.findByIdFetchBlocks(req.getPageId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        if (page.getTemplate() == Template.BLANK) {
            log.error("cannot add block in Blank template");
            throw new IllegalAccessException("cannot add block in Blank template");
        }

        Block block = blockMapper.toEntity(req);
        block.setPage(page);
        BlockRes blockRes = blockMapper.toDto(blockRepository.save(block));

        // 이벤트 전송
        pageEventHandler.postBlock(page.getId(), userId, new BlockEventRes(blockRes.getBlockId(), blockRes.getPageId(), blockRes.getTitle()));

        return blockRes;
    }

    @Transactional
    public boolean updateBlockOrder(BlockOrderReq req, Long userId) {

        List<Block> blockList = blockRepository.findAllByPageId(req.getPageId());

        boolean flag = false;
        for (Block b : blockList) {
            int newOrder = req.getBlockIds().indexOf(b.getId());
            if (newOrder != b.getBlockOrder()) {
                b.updateOrder(newOrder);
                flag = true;
            }
        }
        if (flag)
            pageEventHandler.putBlockOrder(req.getPageId(), userId, req.getBlockIds());

        return true;
    }

    @Transactional
    public void deleteBlock(Long blockId, Long userId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_BLOCK));

        Long pageId = block.getPage().getId();
        blockRepository.delete(block);

        List<Block> blockList = blockRepository.findAllByPageId(pageId);
        int order = 0;
        for (Block b : blockList) b.updateOrder(order++);

        pageEventHandler.deleteBlock(pageId, userId, new BlockIdRes(blockId));
    }
}

