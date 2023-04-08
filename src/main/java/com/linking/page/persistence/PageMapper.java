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

    default PageRes toDto(Page source) { // 문서 리스트 조회 시 사용
        if (source == null) {
            return null;
        }
        PageRes.PageResBuilder builder = PageRes.builder();
        builder
                .pageId(source.getId())
                .title(source.getTitle())
                .groupId(source.getGroup().getId())
                .annotNotiCnt(10);
        //TODO annotation noti 채우기

        return builder.build();
    }

    default PageDetailedRes toDto(  // 페이지 생성 시 사옹
                                    Page source, List<PageCheckRes> pageCheckResList)
    {
        if (source == null) {
            return null;
        }
        PageDetailedRes.PageDetailedResBuilder builder = PageDetailedRes.builder();
        builder
                .pageId(source.getId())
                .title(source.getTitle())
                .groupId(source.getGroup().getId())
                .blockResList(new ArrayList<>())
                .pageCheckResList(pageCheckResList)
                .annotNotiCnt(0);

        return builder.build();
    }


    default PageDetailedRes toDto(  // 페이지 상세 조회 시 사용
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
                .pageOrder(source.getOrder())
                .pageCheckList(new ArrayList<>())
                .blockList(new ArrayList<>());

        return builder.build();
    }
}
