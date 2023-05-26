package com.linking.page.service;

import com.linking.block.dto.BlockDetailRes;
import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.block.domain.Block;
import com.linking.block.persistence.BlockMapper;
import com.linking.block.persistence.BlockRepository;
import com.linking.global.message.ErrorMessage;
import com.linking.sse.EventType;
import com.linking.sse.event.GroupEvent;
import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageDetailedRes;
import com.linking.page.dto.PageRes;
import com.linking.page_check.domain.PageCheck;
import com.linking.page_check.dto.PageCheckRes;
import com.linking.page_check.persistence.PageCheckMapper;
import com.linking.page_check.persistence.PageCheckRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.sse.ssehandler.PageEventHandler;
import com.linking.page.domain.Page;
import com.linking.page.domain.Template;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PageService {

    private final ApplicationEventPublisher publisher;
    private final PageEventHandler pageEventHandler;
    private final PageRepository pageRepository;
    private final PageMapper pageMapper;
    private final GroupRepository groupRepository;
    private final PageCheckRepository pageCheckRepository;
    private final PageCheckMapper pageCheckMapper;
    private final ParticipantRepository participantRepository;
    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;
    private final AnnotationMapper annotationMapper;

    public PageDetailedRes getPage(Long pageId, Set<Long> enteringUserIds) {
        log.info("getPage async test" + Thread.currentThread());

        // toMany는 하나만 Fetch join 가능
        Page page = pageRepository.findByIdFetchPageChecks(pageId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        List<PageCheckRes> pageCheckResList = this.toPageCheckResList(page.getPageCheckList(), enteringUserIds);

        if (page.getTemplate() == Template.BLANK)  // blank 타입의 page
            return pageMapper.toDto(page, pageCheckResList);

        else if(page.getTemplate() == Template.BLOCK) { // block 타입의 page
            List<BlockDetailRes> blockResList = this.toBlockResList(blockRepository.findAllByPageIdFetchAnnotations(page.getId()));
            return pageMapper.toDto(page, blockResList, pageCheckResList);
        }
        return null; // TODO template이 blank, block이 아닌 다른 경우는 없긴 할거 같은데 예외처리 해야겠지,,?
    }

    private List<PageCheckRes> toPageCheckResList(List<PageCheck> pageCheckList, Set<Long> enteringUserIds) {
        List<PageCheckRes> pageCheckResList = new ArrayList<>();

        pageCheckList.forEach(pageCheck -> {
            PageCheckRes pageCheckRes = pageCheckMapper.toDto(pageCheck);
            if (enteringUserIds.contains(pageCheck.getParticipant().getUser().getUserId()))
                pageCheckRes.setIsEntering(true);
            else
                pageCheckRes.setIsEntering(false);
            pageCheckResList.add(pageCheckRes);
        });

//         userName 순으로 정렬
        return pageCheckResList.stream()
                .sorted(Comparator.comparing(PageCheckRes::getUserName))
                .collect(Collectors.toList());
    }

    private List<BlockDetailRes> toBlockResList(List<Block> blockList) {

        if (blockList.isEmpty())
            return blockMapper.toDummyDto();

        List<BlockDetailRes> blockResList = new ArrayList<>();

        for (Block block : blockList) {
            List<AnnotationRes> annotationResList = new ArrayList<>();
            List<Annotation> annotations = block.getAnnotationList();
            if (annotations.isEmpty())
                annotationResList.add(annotationMapper.toDummyDto());
            else {
                for (Annotation annotation : block.getAnnotationList())
                    annotationResList.add(annotationMapper.toDto(annotation));
            }
            blockResList.add(blockMapper.toDto(block, annotationResList));
        }
        return blockResList;
    }

    public PageRes createPage(PageCreateReq req, Long userId) {
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

        publisher.publishEvent(GroupEvent.builder()
                .eventName(EventType.POST_PAGE)
                .projectId(group.getProject().getProjectId())
                .userId(userId)
                .data(pageRes)
                .build());

        return pageRes;
    }

    public void deletePage(Long pageId, Long userId) throws NoSuchElementException{
        log.info("deletePage - {}", this.getClass().getSimpleName());

        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));
        Long projectId = page.getGroup().getProject().getProjectId();
        Long groupId = page.getGroup().getId();
        pageRepository.delete(page);

        publisher.publishEvent(GroupEvent.builder()
                .eventName(EventType.DELETE_PAGE)
                .projectId(projectId)
                .userId(userId)
                .data(PageRes.builder()
                        .groupId(groupId)
                        .pageId(pageId)
                        .build())
                .build());

        pageEventHandler.deletePage(pageId, userId, pageId);

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
