package com.linking.page.persistence;

import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                .order(source.getOrder())
                .title(source.getTitle())
                .groupId(source.getGroup().getId());
        return builder.build();
    }

    default Page toEntity(PageCreateReq source) {
        if (source == null) {
            return null;
        }
        Page.PageBuilder builder = Page.builder();
        builder
                .title(source.getTitle())
                .order(source.getOrder())
                .pageCheckList(new ArrayList<>())
                .blockList(new ArrayList<>());
        return builder.build();
    }
}
