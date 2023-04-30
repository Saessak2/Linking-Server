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
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
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

    @Transactional
    public void updateBlockOrder(BlockOrderReq req, Long userId) {

        List<Block> blockList = blockRepository.findAllByPageId(req.getPageId());
        for (Block b : blockList)
            b.updateOrder(req.getBlockIds().indexOf(b.getId()));

        pageEventHandler.putBlockOrder(req.getPageId(), userId, req.getBlockIds());
    }

    // 블럭을 삭제하면 다른 블럭 순서를 재조정해야해  -업데이트
    // delete + update 한 트랜잭션 안에서 한다는거자나
    // 그러면 update하다가 문제가 발생했어 그러면 delete도 안되는거??
    @Transactional
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

