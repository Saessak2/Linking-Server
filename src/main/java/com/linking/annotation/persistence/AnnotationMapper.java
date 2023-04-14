package com.linking.annotation.persistence;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
                .lastModified(source.getLastModified().format(DateTimeFormatter.ofPattern("YY-MM-dd a HH:mm").withLocale(Locale.forLanguageTag("en"))))
                .userName(source.getUserName());

        return builder.build();
    }

    default AnnotationRes toEmptyDto() {
        AnnotationRes annotationRes = AnnotationRes.builder()
                .annotationId(-1L)
                .blockId(-1L)
                .content("")
                .lastModified("22-01-01 AM 01:01")
                .userName("")
                .build();

        return annotationRes;
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
