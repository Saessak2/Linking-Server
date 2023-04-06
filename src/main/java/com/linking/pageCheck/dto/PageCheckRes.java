package com.linking.pageCheck.dto;

import com.linking.user.dto.UserDetailedRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PageCheckRes {

    private Long pageCheckId;
    private Long pageId;
    private String lastChecked;
    private UserDetailedRes userDetailedRes;
}
