package com.linking.pageCheck.persistence;

import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.dto.PageCheckRes;
import com.linking.user.dto.UserDetailedRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PageCheckMapper {

    default PageCheckRes toDto(PageCheck source, UserDetailedRes userDetailedRes) {
        if (source == null) return null;

        PageCheckRes.PageCheckResBuilder builder = PageCheckRes.builder();
        builder
                .pageCheckId(source.getId())
                .pageId(source.getId())
                .lastChecked(source.getLastChecked().format(DateTimeFormatter.ofPattern("YY-MM-dd HH:mm a")))
                .userDetailedRes(userDetailedRes);
        return builder.build();
    }

    default List<PageCheckRes> toDtoBulk(List<PageCheck> sources) {
        if (sources == null) return null;

        List<PageCheckRes> pageCheckResList = new ArrayList<>();

        for (PageCheck source: sources) {
            PageCheckRes.PageCheckResBuilder builder = PageCheckRes.builder();
            builder
                    .pageCheckId(source.getId())
                    .pageId(source.getPage().getId())
                    .lastChecked(source.getLastChecked().format(DateTimeFormatter.ofPattern("YY-MM-dd HH:mm a")))
                    .userDetailedRes(null);

            pageCheckResList.add(builder.build());
        }
        return pageCheckResList;
    }
}
