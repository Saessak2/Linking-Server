package com.linking.annotation.persistence;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationReq;
import com.linking.annotation.dto.AnnotationRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AnnotationMapper {

    default AnnotationRes toDto(Annotation source) {
        if (source == null) {
            return null;
        }
        AnnotationRes.AnnotationResBuilder builder = AnnotationRes.builder();
        builder
                .annotationId(source.getId())
                .content(source.getContent())
                .blockId(source.getBlock().getId())
                .participantId(source.getParticipant().getParticipantId())
                .userName(source.getUserName())
                .lastModified(source.getLastModified().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")));

        return builder.build();
    }

    default Annotation toEntity(AnnotationCreateReq source) {
        if (source == null) {
            return null;
        }
        Annotation.AnnotationBuilder builder = Annotation.builder();
        builder
                .content(source.getContent())
                .userName(source.getUserName())
                .lastModified(LocalDateTime.now());

        return builder.build();
    }
}
