package com.linking.page.persistence;

import com.linking.block.dto.BlockRes;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageDetailedRes;
import com.linking.page.dto.PageRes;
import com.linking.pageCheck.dto.PageCheckRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PageMapper {

//    default PageRes toEmptyDto() {
//        PageRes pageRes = PageRes.builder()
//                .pageId(-1L)
//                .groupId(-1L)
//                .title("")
//                .annotNotCnt(-1)
//                .build();
//
//        return pageRes;
//    }

    default PageRes toDto(Page source) {
        if (source == null) {
            return null;
        }
        PageRes.PageResBuilder builder = PageRes.builder();
        builder
                .pageId(source.getId())
                .title(source.getTitle())
                .groupId(source.getGroup().getId())
                .order(source.getPageOrder())
                .annotNotiCnt(10);
        //TODO annotation noti 채우기

        return builder.build();
    }

    default PageDetailedRes toDto(
                                    Page source, List<BlockRes> blockResList, List<PageCheckRes> pageCheckResList)
    {
        if (source == null) {
            return null;
        }
        PageDetailedRes.PageDetailedResBuilder builder = PageDetailedRes.builder();
        builder
                .pageId(source.getId())
                .title(source.getTitle())
                .groupId(source.getGroup().getId())
                .blockResList(blockResList)
                .pageCheckResList(pageCheckResList)
                .annotNotiCnt(0);
        //TODO annotation noti 채우기. 페이지조회 하면 annotationNotiCnt가 0으로 설정!

        return builder.build();
    }

    default Page toEntity(PageCreateReq source) {
        if (source == null) {
            return null;
        }
        Page.PageBuilder builder = Page.builder();
        builder
                .title(source.getTitle())
                .pageOrder(source.getOrder());

        return builder.build();
    }
}
