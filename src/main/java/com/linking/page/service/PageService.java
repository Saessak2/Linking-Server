package com.linking.page.service;

import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageRes;
import com.linking.page.dto.PageUpdateOrderReq;
import com.linking.page.dto.PageUpdateTitleReq;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final PageMapper pageMapper;
    private final GroupRepository groupRepository;

    public PageRes createPage(PageCreateReq pageCreateReq) throws Exception{
        // TODO 예외처리
        Group refGroup = groupRepository.getReferenceById(pageCreateReq.getGroupId());

        Page page = pageMapper.toEntity(pageCreateReq);
        page.setGroup(refGroup);

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

    public PageRes updatePagesOrder(PageUpdateOrderReq pageUpdateOrderReq) {

        Page destPage = pageRepository.findById(pageUpdateOrderReq.getPageId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        List<Page> beforeGroupPages = pageRepository.findAllByGroup(pageUpdateOrderReq.getBeforeGroupId());
        beforeGroupPages.removeIf(p -> p.getId().equals(pageUpdateOrderReq.getPageId()));

        List<Page> afterGroupPages = pageRepository.findAllByGroup(pageUpdateOrderReq.getAfterGroupId());
        afterGroupPages.add(pageUpdateOrderReq.getChangedOrder(), destPage);

        int order = 0;
        for (Page page : beforeGroupPages) {
            page.updateOrder(order++);
        }
        order = 0;
        for (Page page : afterGroupPages) {
            page.updateOrder(order++);
        }
    }

    public void deletePage(Long pageId) throws NoSuchElementException{
        pageRepository.delete(
                pageRepository.findById(pageId).orElseThrow(
                        () -> new NoSuchElementException(ErrorMessage.NO_PAGE)
                )
        );
    }

    public PageRes getPage(Long docId) throws NoSuchElementException{
        Page findPage = pageRepository.findById(docId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        PageRes.PageResBuilder builder = PageRes.builder();
        builder
                .pageId(findPage.getId())
                .groupId(findPage.getGroup().getId())
                .title(findPage.getTitle())
                .order(findPage.getOrder())
                .pageCheckResList(null)
                .blockResList(null);
        return builder.build();
    }
}
