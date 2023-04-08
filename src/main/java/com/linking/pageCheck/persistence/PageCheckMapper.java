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
                .pageId(source.getId())
                .isChecked(true)
                .lastChecked(source.getLastChecked().format(DateTimeFormatter.ofPattern("YY-MM-dd a HH:mm").withLocale(Locale.forLanguageTag("en"))))
                .userName(userName)
                .userId(userId);
        return builder.build();
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
