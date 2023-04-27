package com.linking.page.service;

import com.linking.block.dto.BlockRes;
import com.linking.block.persistence.BlockRepository;
import com.linking.block.service.BlockService;
import com.linking.global.message.ErrorMessage;
import com.linking.group.controller.DocumentEventHandler;
import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.page.domain.Template;
import com.linking.page.dto.*;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.dto.PageCheckRes;
import com.linking.pageCheck.persistence.PageCheckRepository;
import com.linking.pageCheck.service.PageCheckService;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PageService {
    private final DocumentEventHandler documentEventHandler;
    private final PageRepository pageRepository;
    private final PageMapper pageMapper;
    private final BlockService blockService;
    private final GroupRepository groupRepository;
    private final PageCheckRepository pageCheckRepository;
    private final ParticipantRepository participantRepository;
    private final PageCheckService pageCheckService;
    private final BlockRepository blockRepository;


    public PageDetailedRes getPage(Long pageId, Long userId, Set<Long> enteringUserIds) {
        // toMany는 하나만 Fetch join 가능
        Page page = pageRepository.findByIdFetchPageChecks(pageId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        List<PageCheckRes> pageCheckResList = pageCheckService.toPageCheckResList(page.getPageCheckList(), userId, enteringUserIds);
        PageDetailedRes pageDetailedRes = null;

        if (page.getTemplate() == Template.BLANK) {
            pageDetailedRes = pageMapper.toDto(page, pageCheckResList);
        } else if(page.getTemplate() == Template.BLOCK) {
            List<BlockRes> blockResList = blockService.toBlockResList(blockRepository.findAllByPageIdFetchAnnotations(page.getId()));
            pageDetailedRes = pageMapper.toDto(page, blockResList, pageCheckResList);
        }

        return pageDetailedRes;
    }

    // TODO code refactoring
    public PageRes createPage(PageCreateReq req, Long userId) throws NoSuchElementException{
        Group group = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        Page page = pageMapper.toEntity(req);
        page.setGroup(group);
        // 페이지 저장을 해야 id를 얻을 수 있음
        pageRepository.save(page);

        // 팀원 마다 pageCheck create
        List<Participant> participants = participantRepository.findAllByProjectId(group.getProject().getProjectId());
        for (Participant participant : participants) {
            PageCheck pageCheck = new PageCheck(participant, page);
            pageCheckRepository.save(pageCheck);
        }
        PageRes pageRes = pageMapper.toDto(pageRepository.save(page), 0);

        documentEventHandler.postPage(group.getProject().getProjectId(), userId, pageRes);

        return pageRes;
    }

    public void deletePage(Long pageId, Long userId) throws NoSuchElementException{
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));
        Long projectId = page.getGroup().getProject().getProjectId();
        Long groupId = page.getGroup().getId();
        pageRepository.delete(page);

        documentEventHandler.deletePage(projectId, userId, new PageIdRes(pageId));

        // 페이지 순서를 0부터 재정렬
        try {
            List<Page> pageList = pageRepository.findAllByGroupId(groupId);
            int order = 0;
            for (Page p : pageList) {
                if (p.getPageOrder() != order) {
                    p.updateOrder(order);
                    pageRepository.save(p);
                }
                order++;
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
