package com.linking.pageCheck.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PageCheckRes {

    private Long pageCheckId;
    private Long pageId;
    private Boolean isChecked;
    private String lastChecked;
    private Long userId;
    private String userName;

}
