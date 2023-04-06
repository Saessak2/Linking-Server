package com.linking.annotation.persistence;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
                .blockId(source.getBlock().getId())
                .content(source.getContent())
                .lastModified(source.getLastModified().format(DateTimeFormatter.ofPattern("YY-MM-dd")))
                .userName(source.getUserName());

        return builder.build();
    }

    default List<AnnotationRes> toDtoBulk(List<Annotation> sources) {
        if (sources == null) return null;

        List<AnnotationRes> annotationResList = new ArrayList<>();

        for (Annotation source : sources) {
            AnnotationRes.AnnotationResBuilder builder = AnnotationRes.builder();
            builder
                    .annotationId(source.getId())
                    .blockId(source.getBlock().getId())
                    .content(source.getContent())
                    .lastModified(source.getLastModified().format(DateTimeFormatter.ofPattern("YY-MM-dd")))
                    .userName(source.getUserName());
        }
        return annotationResList;
    }

    default Annotation toEntity(AnnotationCreateReq source) {
        if (source == null) {
            return null;
        }
        Annotation.AnnotationBuilder builder = Annotation.builder();
        builder
                .content(source.getContent())
                .userName(source.getUserName())
                .lastModified(LocalDate.now());

        return builder.build();
    }
}
