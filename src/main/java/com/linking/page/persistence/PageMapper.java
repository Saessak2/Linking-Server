package com.linking.page.persistence;

import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
import com.linking.page.dto.PageRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                .createdDatetime(source.getCreatedDatetime().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")))
                .updatedDatetime(source.getUpdatedDatetime().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")))
                .projectId(source.getProject().getProjectId())
                .parentDocId(source.getParent().getId());

        return builder.build();
    }

    default Page toEntity(PageCreateReq source) {
        if (source == null) {
            return null;
        }
        Page.PageBuilder builder = Page.builder();
        builder
                .docIndex(source.getDocIndex())
                .title(source.getTitle())
                .createdDatetime(LocalDateTime.now())
                .updatedDatetime(LocalDateTime.now());

        return builder.build();
    }
}
