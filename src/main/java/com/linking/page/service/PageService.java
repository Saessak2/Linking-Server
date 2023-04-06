package com.linking.page.service;

import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageRes;
import com.linking.page.dto.PageUpdateTitleReq;
import com.linking.page.persistence.PageMapper;
import com.linking.page.persistence.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final PageMapper pageMapper;
    private final GroupRepository groupRepository;


    public PageRes getPage(Long pageId) throws NoSuchElementException{
        Page findPage = pageRepository.findById(pageId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE));

        return pageMapper.toDetailDto(findPage);
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
