package com.linking.page.persistence;

import com.linking.block.dto.BlockRes;
import com.linking.page.domain.Page;
import com.linking.page.dto.*;
import com.linking.pageCheck.dto.PageCheckRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PageMapper {

    default PageRes toDto(Page source, int annoNotiCnt) {
        if (source == null) {
            return null;
        }
        PageRes.PageResBuilder builder = PageRes.builder();
        builder
                .pageId(source.getId())
                .title(source.getTitle())
                .groupId(source.getGroup().getId())
                .template(source.getTemplate())
                .annoNotCnt(annoNotiCnt);

        return builder.build();
    }

    default BlockPageDetailRes toDto(
                                    Page source, List<BlockRes> blockResList, List<PageCheckRes> pageCheckResList)
    {
        BlockPageDetailRes builder = BlockPageDetailRes.builder()
                .pageId(source.getId())
                .title(source.getTitle())
                .groupId(source.getGroup().getId())
                .pageCheckResList(pageCheckResList)
                .blockResList(blockResList)
                .build();

        return builder;
    }

    default BlankPageDetailRes toDto(
            Page source, List<PageCheckRes> pageCheckResList)
    {
        BlankPageDetailRes builder = BlankPageDetailRes.builder()
                .pageId(source.getId())
                .title(source.getTitle())
                .groupId(source.getGroup().getId())
                .pageCheckResList(pageCheckResList)
                .build();

        return builder;

    }

    default Page toEntity(PageCreateReq source) {
        if (source == null) {
            return null;
        }
        Page.PageBuilder builder = Page.builder();
        builder
                .title(source.getTitle())
                .pageOrder(source.getOrder())
                .template(source.getTemplate());

        return builder.build();
    }
}