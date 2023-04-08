package com.linking.page.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.block.domain.Block;
import com.linking.block.dto.BlockRes;
import com.linking.block.persistence.BlockMapper;
import com.linking.block.service.BlockService;
import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageDetailedRes;
import com.linking.page.dto.PageRes;
import com.linking.page.dto.PageUpdateTitleReq;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.dto.PageCheckRes;
import com.linking.pageCheck.persistence.PageCheckMapper;
import com.linking.pageCheck.persistence.PageCheckRepository;
import com.linking.pageCheck.service.PageCheckService;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.user.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
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
    // TODO code refactoring
    public PageDetailedRes getPage(Long pageId, Long userId) throws NoSuchElementException{
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        List<PageCheckRes> pageCheckResList = pageCheckService.getPageCheckResList(page, userId);
        List<BlockRes> blockResList = blockService.getBlockResList(page);

        List<PageCheckRes> sortedPageCheckList = pageCheckResList.stream()
                .sorted(Comparator.comparing(PageCheckRes::getUserName))
                .collect(Collectors.toList());

        return pageMapper.toDto(page, blockResList, sortedPageCheckList);
    }

    // TODO code refactoring
    public PageDetailedRes createPage(PageCreateReq req) throws NoSuchElementException{
        Group group = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        Page page = pageMapper.toEntity(req);
        page.setGroup(group);
        // 페이지가 저장이 안돼서 id 가 없음.
        pageRepository.save(page);

        List<Participant> participants = participantRepository.findByProject(group.getProject());
        List<PageCheckRes> pageCheckResList = new ArrayList<>();
        for (Participant participant : participants) {
            PageCheck pageCheck = new PageCheck(participant, page);
            pageCheckRepository.save(pageCheck);
//            pageCheckResList.add(pageCheckMapper.toDto(pageCheck, userMapper.toDtoTemp(pageCheck.getParticipant().getUser())));
        }

        List<PageCheckRes> sortedPageCheckList = pageCheckResList.stream()
                .sorted(Comparator.comparing(PageCheckRes::getUserName))
                .collect(Collectors.toList());

        return pageMapper.toDto(pageRepository.save(page), sortedPageCheckList);
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

        try {
            List<Page> pageList = pageRepository.findAllByGroupId(groupId);
            updateOrder(pageList);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void updateOrder(List<Page> pageList) {
        int idx = 0;
        for (Page page : pageList)
            page.updateOrder(idx++);
        pageRepository.saveAll(pageList);
    }
}
