package com.linking.pageCheck.persistence;

import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.dto.PageCheckRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PageCheckMapper {

    default PageCheckRes toDto(PageCheck source, String userName, Long userId) {
        if (source == null) return null;

        PageCheckRes.PageCheckResBuilder builder = PageCheckRes.builder();
        builder
                .pageCheckId(source.getId())
                .pageId(source.getId());
        if (source.getLastChecked() == null)
            builder
                    .isChecked(false)
                    .lastChecked("23-01-01 AM 01:01");
        else
            builder
                    .isChecked(true)
                    .lastChecked(source.getLastChecked().format(DateTimeFormatter.ofPattern("YY-MM-dd a HH:mm").withLocale(Locale.forLanguageTag("en"))));
        builder
                .userName(userName)
                .userId(userId);
        return builder.build();
    }

    default PageCheckRes toEmptyDto() {
        PageCheckRes builder = PageCheckRes.builder()
                .pageCheckId(-1L)
                .lastChecked("23-01-01 AM 01:01").build();
        return builder;
    }
//    default List<PageCheckRes> toDtoBulk(List<PageCheck> sources) {
//        if (sources == null) return null;
//
//        List<PageCheckRes> pageCheckResList = new ArrayList<>();
//
//        for (PageCheck source: sources) {
//            PageCheckRes.PageCheckResBuilder builder = PageCheckRes.builder();
//            builder
//                    .pageCheckId(source.getId())
//                    .pageId(source.getPage().getId())
//                    .lastChecked(source.getLastChecked().format(DateTimeFormatter.ofPattern("YY-MM-dd hh:mm a").withLocale(Locale.forLanguageTag("en"))))
//                    .userDetailedRes(null);
//
//            pageCheckResList.add(builder.build());
//        }
//        return pageCheckResList;
//    }
}
