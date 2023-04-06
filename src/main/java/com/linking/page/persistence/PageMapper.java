package com.linking.page.persistence;

import com.linking.block.dto.BlockRes;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
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

    default PageRes toDto(
            Page source, List<BlockRes> blockResList, List<PageCheckRes> pageCheckResList)
    {
        if (source == null) {
            return null;
        }
        PageRes.PageResBuilder builder = PageRes.builder();
        builder
                .pageId(source.getId())
                .title(source.getTitle())
                .groupId(source.getGroup().getId())
                .blockResList(blockResList)
                .pageCheckResList(pageCheckResList)
                .annotNotiCnt(0);
        //TODO annotation noti 채우기

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
