package com.linking.pageCheck.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "페이지 열람 시간 DTO")
public class PageCheckRes {

    @Schema(description = "page check id")
    private Long pageCheckId;

    @Schema(description = "page id")
    private Long pageId;
    @Schema(description = "현재 조회 중인지 여부")
    private Boolean isEntering;

    @Schema(description = "페이지 확인 여부")
    private Boolean isChecked;

    @Schema(description = "페이지 마지막 열람 시간 (페이지 열람한 시간 or 나간 시간)")
    private String lastChecked;

    @Schema(description = "user id")
    private Long userId;

    @Schema(description = "user full name (성+이름)")
    private String userName;

    public void setIsEntering(boolean isEntering) {
        this.isEntering = isEntering;
    }
}
