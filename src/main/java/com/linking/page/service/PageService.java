package com.linking.page.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.block.domain.Block;
import com.linking.block.dto.BlockRes;
import com.linking.block.persistence.BlockMapper;
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
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.user.domain.User;
import com.linking.user.dto.UserDetailedRes;
import com.linking.user.dto.UserTempRes;
import com.linking.user.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final GroupRepository groupRepository;
    private final BlockMapper blockMapper;
    private final PageCheckMapper pageCheckMapper;
    private final AnnotationMapper annotationMapper;
    private final PageCheckRepository pageCheckRepository;
    private final ParticipantRepository participantRepository;
    private final UserMapper userMapper;


    // TODO 한번에 select 날리는 방법 찾아보기
    // TODO code refactoring
    public PageDetailedRes getPage(Long pageId) throws NoSuchElementException{
        Page findPage = pageRepository.findById(pageId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        List<PageCheckRes> pageCheckResList = new ArrayList<>();
        for (PageCheck pageCheck : findPage.getPageCheckList()) {
            Participant participant = participantRepository.findById(pageCheck.getParticipant().getParticipantId()).get();
//            UserTempRes userRes = userMapper.toDtoTemp(participant.getUser());
            User user = participant.getUser();

            pageCheck.updateLastChecked(); // 페이지 확인 시간 업뎃
            pageCheckRepository.save(pageCheck);
            pageCheckResList.add(pageCheckMapper.toDto(pageCheck, user.getLastName()+user.getFirstName(), user.getUserId()));
        }

        List<BlockRes> blockResList = new ArrayList<>();
        for (Block block : findPage.getBlockList()) {
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

        List<PageCheckRes> sortedPageCheckList = pageCheckResList.stream()
                .sorted(Comparator.comparing(PageCheckRes::getUserName))
                .collect(Collectors.toList());


        return pageMapper.toDto(findPage, blockResList, sortedPageCheckList);
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

//    public PageRes updatePagesOrder(PageUpdateOrderReq pageUpdateOrderReq) {
//
//        Page destPage = pageRepository.findById(pageUpdateOrderReq.getPageId())
//                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));
//
//        List<Page> beforeGroupPages = pageRepository.findAllByGroup(pageUpdateOrderReq.getBeforeGroupId());
//        beforeGroupPages.removeIf(p -> p.getId().equals(pageUpdateOrderReq.getPageId()));
//
//        List<Page> afterGroupPages = pageRepository.findAllByGroup(pageUpdateOrderReq.getAfterGroupId());
//        afterGroupPages.add(pageUpdateOrderReq.getOrder(), destPage);
//
//        int order = 0;
//        for (Page page : beforeGroupPages) {
//            page.updateOrder(order++);
//            pageRepository.save(page);
//        }
//        order = 0;
//        for (Page page : afterGroupPages) {
//            page.updateOrder(order++);
//            pageRepository.save(page);
//        }
//        return pageMapper.toDto(destPage);
//    }

    public void deletePage(Long pageId) throws NoSuchElementException{
        pageRepository.delete(
                pageRepository.findById(pageId).orElseThrow(
                        () -> new NoSuchElementException(ErrorMessage.NO_PAGE)
                )
        );
    }
}
