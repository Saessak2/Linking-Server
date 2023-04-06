package com.linking.page.persistence;

import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PageMapper {

    default PageRes toDto(Page source) {
        if (source == null) {
            return null;
        }
        PageRes.PageResBuilder builder = PageRes.builder();
        builder
                .pageId(source.getId())
                .title(source.getTitle())
                .groupId(source.getGroup().getId())
                .blockResList(new ArrayList<>())
                .pageCheckResList(new ArrayList<>())
                .annotNotiCnt(0);
        //TODO annotation noti 채우기

        return builder.build();
    }

    default PageRes toDetailDto(Page source) {
        if (source == null) {
            return null;
        }
        PageRes.PageResBuilder builder = PageRes.builder();
        builder
                .pageId(source.getId())
                .title(source.getTitle())
                .groupId(source.getGroup().getId())
                .blockResList(null)
                .pageCheckResList(null)
                .annotNotiCnt(0);
        //TODO annotation noti 채우기
        //TODO 자식 리스트 채우기

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
