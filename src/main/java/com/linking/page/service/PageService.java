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
import com.linking.page.dto.PageRes;
import com.linking.page.dto.PageUpdateTitleReq;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.dto.PageCheckRes;
import com.linking.pageCheck.persistence.PageCheckMapper;
import com.linking.pageCheck.persistence.PageCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    public PageRes getPage(Long pageId) throws NoSuchElementException{
        Page findPage = pageRepository.findById(pageId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        List<PageCheckRes> pageCheckResList = new ArrayList<>();
        for (PageCheck pageCheck : findPage.getPageCheckList()) {
            pageCheck.updateLastChecked();
            pageCheckRepository.save(pageCheck);
            pageCheckResList.add(pageCheckMapper.toDto(pageCheck));
        }

        List<BlockRes> blockResList = new ArrayList<>();
        for (Block block : findPage.getBlockList()) {
            List<AnnotationRes> annotationResList = new ArrayList<>();

            for (Annotation annotation : block.getAnnotationList()) {
                annotationResList.add(annotationMapper.toDto(annotation));
            }
            blockResList.add(blockMapper.toDto(block, annotationResList));
        }

        return pageMapper.toDto(findPage, blockResList, pageCheckResList);
    }

    public PageRes createPage(PageCreateReq pageCreateReq) throws NoSuchElementException{
        Group group = groupRepository.findById(pageCreateReq.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        Page page = pageMapper.toEntity(pageCreateReq);
        page.setGroup(group);

        return pageMapper.toDto(pageRepository.save(page));
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
