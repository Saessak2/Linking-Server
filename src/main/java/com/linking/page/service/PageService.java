package com.linking.page.service;

import com.linking.block.dto.BlockRes;
import com.linking.block.service.BlockService;
import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final PageMapper pageMapper;
    private final BlockService blockService;
    private final GroupRepository groupRepository;
    private final PageCheckRepository pageCheckRepository;
    private final ParticipantRepository participantRepository;
    private final PageCheckService pageCheckService;


    // TODO 한번에 select 날리는 방법 찾아보기
    public Optional<PageDetailedRes> getPage(Long pageId, Long userId) throws NoSuchElementException{
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        List<PageCheckRes> pageCheckResList = pageCheckService.getPageCheckList(page, userId);
        List<BlockRes> blockResList = blockService.getBlockResList(page);

        List<PageCheckRes> sortedPageCheckList = pageCheckResList.stream()
                .sorted(Comparator.comparing(PageCheckRes::getUserName))
                .collect(Collectors.toList());

        return Optional.ofNullable(pageMapper.toDto(page, blockResList, sortedPageCheckList));
    }

    // TODO code refactoring
    public PageDetailedRes createPage(PageCreateReq req) throws NoSuchElementException{
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

        return pageMapper.toDto(pageRepository.save(page), new ArrayList<>(), new ArrayList<>());
    }

    public PageRes updatePageTitle(PageUpdateTitleReq pageUpdateTitleReq) throws Exception{

        try {
            pageRepository.updateTitle(pageUpdateTitleReq.getPageId(), pageUpdateTitleReq.getTitle());

            Page findPage = pageRepository.findById(pageUpdateTitleReq.getPageId())
                    .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

            return pageMapper.toDto(findPage);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            throw new NoSuchElementException(ErrorMessage.NO_PAGE);
        }
    }

    public void deletePage(Long pageId) throws NoSuchElementException{
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));
        Long groupId = page.getGroup().getId();
        pageRepository.delete(page);

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
