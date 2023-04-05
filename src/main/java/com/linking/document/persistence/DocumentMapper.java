package com.linking.document.persistence;

import com.linking.document.domain.DType;
import com.linking.document.domain.Document;
import com.linking.document.dto.DocumentRes;
import com.linking.group.dto.GroupCreateReq;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageCreateReq;
import org.mapstruct.Mapper;

import java.util.ArrayList;

@Mapper(
        componentModel = "spring"
)
public interface DocumentMapper {

    default DocumentRes toDto(Document source) {
        if (source == null) {
            return null;
        }
        DocumentRes.DocumentResBuilder builder = DocumentRes.builder();
        builder
                .docId(source.getId())
                .title(source.getTitle())
                .docIndex(source.getDocIndex())
                .DType(source.getDType().toString());
        return builder.build();
    }

    default Document toEntity(GroupCreateReq source) {
        if (source == null) {
            return null;
        }
        Document.DocumentBuilder builder = Document.builder();
        builder
                .title(source.getTitle())
                .docIndex(source.getDocIndex())
                .dType(DType.GROUP)
                .childList(new ArrayList<>());
        return builder.build();
    }

    default Document toEntity(PageCreateReq source) {
        if (source == null) {
            return null;
        }
        Document.DocumentBuilder builder = Document.builder();
        builder
                .title(source.getTitle())
                .docIndex(source.getDocIndex())
                .dType(DType.PAGE)
                .childList(null);
        return builder.build();
    }
}
